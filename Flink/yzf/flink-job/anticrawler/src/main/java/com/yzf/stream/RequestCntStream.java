package com.yzf.stream;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.yzf.bean.NginxLogFormatter;
import com.yzf.bean.RequestCnt;
import com.yzf.bean.TokenInfo;
import com.yzf.common.WebUrl;
import com.yzf.config.Configuration;
import com.yzf.sink.CSMHttpAlarmSink;
import com.yzf.source.NginxLogKafkaSource;
import com.yzf.util.CookieUtil;
import com.yzf.util.TimeFormatUtil;
import com.yzf.util.TokenInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.lang.reflect.Executable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：
 * 从Kafka中获取nginx日志，提取Cookie中的access_token字段，并获取其中用户名、密码。统计一段时间内请求数量。
 * 结果发送到Kafka中
 * @date ：2021/1/28 下午4:07
 */


public class RequestCntStream {
    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 0、将kafka中的json转为pojo类ddlBinLog
        DataStream<ObjectNode> nginxKafkaSource = NginxLogKafkaSource.getKafkaSource(env);
        // 1、解析ddlBinLog的cookie、access_token，获得用户纬度信息。
        DataStream<NginxLogFormatter> nginxLogFormatterDataStream = nginxKafkaSource
                .flatMap(new FlatMapFunction<ObjectNode, NginxLogFormatter>() {

            @Override
            public void flatMap(ObjectNode jsonNodes, Collector<NginxLogFormatter> collector) throws Exception {
                NginxLogFormatter nginxLogFormatter = new NginxLogFormatter(
                );
                try{
                    // 1.1、解析json
                    nginxLogFormatter.setRemote_addrx(jsonNodes.get("value").get("remote_addrx").asText());

                    nginxLogFormatter.setHttp_referer(jsonNodes.get("value").get("http_referer").asText());
                    nginxLogFormatter.setTimestamp(jsonNodes.get("value").get("@timestamp").asText());
                    String time_local = TimeFormatUtil.transformer(jsonNodes.get("value").get("time_local").asText());
                    nginxLogFormatter.setTime_local(time_local);
                    nginxLogFormatter.setNginx(jsonNodes.get("value").get("nginx").asText());
                    nginxLogFormatter.setFields_set(jsonNodes.get("value").get("fields-set").asText());
                    nginxLogFormatter.setFields_ip(jsonNodes.get("value").get("fields-ip").asText());
                    nginxLogFormatter.setHttp_user_agent(jsonNodes.get("value").get("http_user_agent").asText());
                    nginxLogFormatter.setRequest(jsonNodes.get("value").get("request").asText());
                    nginxLogFormatter.setStatus(jsonNodes.get("value").get("status").asText());
                    nginxLogFormatter.setX_forwarded_for(jsonNodes.get("value").get("x_forwarded_for").asText());
                    nginxLogFormatter.setProxy_add_x_forwarded_for(jsonNodes.get("value").get("proxy_add_x_forwarded_for").asText());
                    nginxLogFormatter.setCookie(jsonNodes.get("value").get("http_cookie").asText());

                }catch (Exception e){
                    e.printStackTrace();
                }

                collector.collect(nginxLogFormatter);
            }
        }).flatMap(new FlatMapFunction<NginxLogFormatter, NginxLogFormatter>() {
            @Override
            public void flatMap(NginxLogFormatter nginxLog, Collector<NginxLogFormatter> collector) throws Exception {
                // 1.2、清洗出请求路径和请求参数，如果请求路径可以找到对应的页面则继续，否则丢弃这条日志
                // TODO 将GET、POST、HTTP等的去掉

                String[] string_list = StringUtils.split(nginxLog.getRequest()," ");
                if(string_list.length ==3){
                    String request_url = string_list[1];
                    nginxLog.setRequest_base_url(StringUtils.substringBefore(request_url,"?"));
                    nginxLog.setRequest_params(StringUtils.substringAfter(request_url,"?"));
                    if(WebUrl.urlAndWebPage.containsKey(nginxLog.getRequest_base_url())){
                        nginxLog.setRequest_web_page(WebUrl.urlAndWebPage.get(nginxLog.getRequest_base_url()));
                        collector.collect(nginxLog);
                    }
                }

            }
        }).flatMap(new FlatMapFunction<NginxLogFormatter, NginxLogFormatter>() {
            @Override
            public void flatMap(NginxLogFormatter nginxLog, Collector<NginxLogFormatter> collector) throws Exception {
                // 1.3、解析token
                String access_token = CookieUtil.getCookieValueByCookieKey(nginxLog.getCookie(),"access_token");

                try {
                    TokenInfo tokenInfo = TokenInfoUtil.decode(access_token);
                    nginxLog.setUserId(tokenInfo.getUserId());
                    nginxLog.setUserName(tokenInfo.getUserName());
                    nginxLog.setGsId(tokenInfo.getGsId());
                    nginxLog.setPhone(tokenInfo.getPhone());
                    collector.collect(nginxLog);

                }catch (JWTDecodeException e){
                    System.err.println("非法token："+e.getMessage()+"用户id"
                    +nginxLog.getUserId());
                }catch (Exception e){
                    System.err.println(e.getMessage()+"用户id"+nginxLog.getRequest());
                }

            }
        });
        // 2.按照用户id、访问页面、请求所在的一分钟，进行分组
        // TODO 这里要使用count，不再做UA的去重
        DataStream<RequestCnt> requestCntDataStream = nginxLogFormatterDataStream.keyBy(new KeySelector<NginxLogFormatter, Object>() {
            @Override
            // 为避免nginx网络问题导致滞后，这里采用用户id、请求页面、以及请求所在的那一分钟进行分组。以此保证在使用事件时间开窗的情况下，不丢弃数据，做到预警
            public Object getKey(NginxLogFormatter nginxLog) throws Exception {
                // nginxLog.getTime_local().substring(0,16)
                return nginxLog.getUserId()+nginxLog.getRequest_web_page()+nginxLog.getTime_local().substring(0,16);
            }
        }).window(TumblingProcessingTimeWindows.of(Time.minutes(3L)))
                .apply(new WindowFunction<NginxLogFormatter, RequestCnt, Object, TimeWindow>() {
                    @Override
                    public void apply(Object o, TimeWindow timeWindow, Iterable<NginxLogFormatter> iterable, Collector<RequestCnt> collector) throws Exception {
                        RequestCnt requestCnt = new RequestCnt();
                        Set<String> ua = new HashSet<>();
                        Integer request_cnt = 0;
                        for(NginxLogFormatter nginxLog:iterable){
                            request_cnt ++;
                            requestCnt.setUserId(nginxLog.getUserId());
                            requestCnt.setUserName(nginxLog.getUserName());
                            requestCnt.setGsId(nginxLog.getGsId());
                            requestCnt.setRequest_web_page(nginxLog.getRequest_web_page());
                            requestCnt.setRemote_addrx(nginxLog.getRemote_addrx());
                            ua.add(nginxLog.getHttp_user_agent());
                            requestCnt.setLastTime(nginxLog.getTime_local());
                        }
                        requestCnt.setUA_CNT(ua.size());
                        requestCnt.setRequest_cnt(request_cnt);
                        collector.collect(requestCnt);
                    }
                });

        // 3、保存结果
        // 3.1、将NginxLogFormatter发送到kafka中缓存结果。
        DataStream<String> nginxSink = nginxLogFormatterDataStream.flatMap(new FlatMapFunction<NginxLogFormatter, String>() {
            @Override
            public void flatMap(NginxLogFormatter nginxLog, Collector<String> collector) throws Exception {
                String a = JSON.toJSONString(nginxLog);
                collector.collect(a);
            }
        });
//        nginxSink.addSink(new FlinkKafkaProducer<String>(Configuration.PROPERTIES.getProperty("bootstrap.servers"),Configuration.PROPERTIES.getProperty("nginx_log_anticrawler_formatter.topic"), new SimpleStringSchema()));
        // 3.2、将RequestCnt发送到kafka中缓存结果。
        DataStream<String> requestCntDataStreamSink = requestCntDataStream.flatMap(new FlatMapFunction<RequestCnt, String>() {
            @Override
            public void flatMap(RequestCnt analyseResult, Collector<String> collector) throws Exception {
                String a = JSON.toJSONString(analyseResult);
                collector.collect(a);
            }
        });
//        requestCntDataStreamSink.addSink(new FlinkKafkaProducer<String>(Configuration.PROPERTIES.getProperty("bootstrap.servers"),Configuration.PROPERTIES.getProperty("nginx_log_anticrawler_request_cnt.topic"), new SimpleStringSchema()));

        // 4、过滤出RequestCnt中请求大于阈值的个数，作为预警数据
        DataStream<RequestCnt> alarm = requestCntDataStream.filter(new FilterFunction<RequestCnt>() {
            @Override
            public boolean filter(RequestCnt requestCnt) throws Exception {
                if(requestCnt.getRequest_cnt()>=Integer.valueOf(Configuration.PROPERTIES.getProperty("alarm_requect_per_mintue"))){
                    return true;
                }else return false;
            }
        });
        // 5、将预警数据发送给CSM。
        alarm.addSink(new CSMHttpAlarmSink());
//        nginxSink.print();
//        requestCntDataStream.print();
        env.execute("反爬数据项目分析");
    }
}
