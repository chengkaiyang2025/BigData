package com.yzf.stream;

import com.yzf.bean.RequestCnt;
import com.yzf.bean.NginxLogFormatter;
import com.yzf.source.NginxLogKafkaSource;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：（实验）分析IP的分布
 * @date ：2021/1/27 下午7:41
 */


public class AntiCrawlerByIp {
//    Logger LOG = LoggerFactory.getLogger(AntiCrawlerByIp.class);
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<ObjectNode> ddlBinLog = NginxLogKafkaSource.getKafkaSource(env);
        DataStream<NginxLogFormatter> nginxSource = ddlBinLog.flatMap(new FlatMapFunction<ObjectNode, NginxLogFormatter>() {
            @Override
            public void flatMap(ObjectNode jsonNodes, Collector<NginxLogFormatter> collector) throws Exception {
                NginxLogFormatter nginxLogFormatter = new NginxLogFormatter(

                );

                nginxLogFormatter.setRemote_addrx(jsonNodes.get("value").get("remote_addrx").asText());

                nginxLogFormatter.setHttp_referer(jsonNodes.get("value").get("http_referer").asText());
                nginxLogFormatter.setTimestamp(jsonNodes.get("value").get("@timestamp").asText());
                nginxLogFormatter.setTime_local(jsonNodes.get("value").get("time_local").asText());
                nginxLogFormatter.setNginx(jsonNodes.get("value").get("nginx").asText());
                nginxLogFormatter.setFields_set(jsonNodes.get("value").get("fields-set").asText());
                nginxLogFormatter.setFields_ip(jsonNodes.get("value").get("fields-ip").asText());
                nginxLogFormatter.setHttp_user_agent(jsonNodes.get("value").get("http_user_agent").asText());
                nginxLogFormatter.setRequest(jsonNodes.get("value").get("request").asText());
                nginxLogFormatter.setStatus(jsonNodes.get("value").get("status").asText());
                nginxLogFormatter.setX_forwarded_for(jsonNodes.get("value").get("x_forwarded_for").asText());
                nginxLogFormatter.setProxy_add_x_forwarded_for(jsonNodes.get("value").get("proxy_add_x_forwarded_for").asText());
                collector.collect(nginxLogFormatter);
            }
        });

        DataStream<NginxLogFormatter> mapedNginxSource = nginxSource.flatMap(new FlatMapFunction<NginxLogFormatter, NginxLogFormatter>() {
            @Override
            public void flatMap(NginxLogFormatter nginxLog, Collector<NginxLogFormatter> collector) throws Exception {

            }
        });


        DataStream<RequestCnt> analyseResultDataStream = nginxSource.keyBy(new KeySelector<NginxLogFormatter, Object>() {
            @Override
            public Object getKey(NginxLogFormatter nginxLog) throws Exception {
                return nginxLog.getRemote_addrx();
            }
        }).window(SlidingProcessingTimeWindows.of(Time.seconds(60L),Time.seconds(5L)))
                .apply(new WindowFunction<NginxLogFormatter, RequestCnt, Object, TimeWindow>() {
                    @Override
                    public void apply(Object o, TimeWindow timeWindow, Iterable<NginxLogFormatter> iterable, Collector<RequestCnt> collector) throws Exception {
                        RequestCnt analyseResult = new RequestCnt();
                        List<String> request_path = new ArrayList<>();
                        Set<String> ua_set = new HashSet<>();
                        Integer request_cnt = 0;
                        String lastTime = "";
                        for(NginxLogFormatter nginxLog:iterable){
                            request_cnt ++;
                            request_path.add(nginxLog.getRequest());
                            ua_set.add(nginxLog.getHttp_user_agent());
                            analyseResult.setRemote_addrx(nginxLog.getRemote_addrx());
                            lastTime = nginxLog.getTime_local();
                        }
                        analyseResult.setLastTime(lastTime);
                        analyseResult.setUA_SET(ua_set);
                        analyseResult.setUA_CNT(analyseResult.getUA_SET().size());
                        analyseResult.setRequest_cnt(request_cnt);
                        collector.collect(analyseResult);
                    }
                });

        DataStream<RequestCnt> analyseResultDataStreamFilter = analyseResultDataStream.filter(new FilterFunction<RequestCnt>() {
            @Override
            public boolean filter(RequestCnt analyseResult) throws Exception {
                return analyseResult.getRequest_cnt()>3;
            }
        });
        nginxSource.print();
//        env.setParallelism(1);
        env.execute("BinLog监控");
    }
}
