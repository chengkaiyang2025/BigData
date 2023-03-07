package com.yzf.di.newdzlogsinkhive.stream;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.bean.TokenInfo;
import com.yzf.di.newdzlogsinkhive.bean.UserAgent;
import com.yzf.di.newdzlogsinkhive.sink.HiveSink;
import com.yzf.di.newdzlogsinkhive.source.KafkaSource;
import com.yzf.di.newdzlogsinkhive.util.CookieUtil;
import com.yzf.di.newdzlogsinkhive.util.TimeFormatUtil;
import com.yzf.di.newdzlogsinkhive.util.TokenInfoUtil;
import com.yzf.di.newdzlogsinkhive.util.UserAgentUtil;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 测试用，用于验证 http://gitlab.yzf.net/group_di/bigdata/flink-job/issues/21
 */
public class NginxAnalysisStreamMap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // TODO 线下测试！
        String bootstrapSource = parameterTool.get("bootstrapSource","172.16.190.35:9092,172.16.190.36:9092,172.16.190.37:9092");
//        String bootstrapSource = parameterTool.get("bootstrapSource","172.24.10.2:9092,172.24.10.3:9092,172.24.10.4:9092,172.24.10.5:9092");

        String hiveConfDir = parameterTool.get("hive_conf_dir","/data/flink/emr_hive_conf/conf");
        int parallelism = 8;

        // 2小时做一次checkpoint
        env.enableCheckpointing(1000*60*60*2);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        DataStream<LogBean> kafkaSource = new KafkaSource().source(env,bootstrapSource,parallelism);

        SingleOutputStreamOperator<LogBean> toJson = kafkaSource.filter(new FilterFunction<LogBean>() {
            @Override
            public boolean filter(LogBean value) throws Exception {
                // fields-set 为新代帐
                return value.getRequest() != null ;
            }
        }).setParallelism(parallelism)
                .map(new MapFunction<LogBean, LogBean>() {
                    @Override
                    public LogBean map(LogBean logBean) throws Exception {


                        try {
                            TokenInfo decode;
                            String cookieString = logBean.getHttp_cookie();
                            String access_token = CookieUtil.getCookieValueByCookieKey(cookieString, "access_token");
                            decode = TokenInfoUtil.decode(access_token);
                            logBean.setCookie_gs_id(decode.getGsId());
                            logBean.setCookie_phone(decode.getPhone());
                            logBean.setCookie_user_id(decode.getUserId());
                            logBean.setCookie_user_name(decode.getUserName());


                        }catch (Exception e){
//            e.printStackTrace();
                            // TODO 测输出流收集失败信息
                        }

                        try {
                            // 通过UA解析处用户使用的浏览器等相关信息

                            UserAgent userAgent ;

                            String uaString = logBean.getHttp_user_agent();
                                userAgent = UserAgentUtil.get(uaString);
                            logBean.setUa_browser_version_info(userAgent.getBrowserVersionInfo());
                            logBean.setUa_device_type(userAgent.getDeviceType());
                            logBean.setUa_os_family(userAgent.getOsFamily());
                            logBean.setUa_os_name(userAgent.getOsName());
                            logBean.setUa_type(userAgent.getType());
                            logBean.setUa_ua_family(userAgent.getUaFamily());
                            logBean.setUa_ua_name(userAgent.getUaName());
                        }catch (Exception e){
//            e.printStackTrace();
                            // TODO 测输出流收集失败信息
                        }
                        return logBean;
                    }
                }).setParallelism(parallelism)
                ;


//        new HiveSink().sink(env,toJson,hiveConfDir);

//        toJson.addSink(new FlinkKafkaProducer<String>(bootstrapTarget,"nginx_log_anticrawler_formatter",new SimpleStringSchema()))
//                ;
        env.execute("nginx实时数据分析-1、解析cookie与ua信息-MAP");
    }

}
