package com.yzf.di;

import com.yzf.di.bean.AlertTyre;
import com.yzf.di.bean.LogMonitorBeans;
import com.yzf.di.function.MyMapFunction2;
import com.yzf.di.function.MyReduceFunction;
import com.yzf.di.sink.SendAlertSink;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/4 15:00
 */
public class Kafka2DingDingMain {
    private static final Logger logger = LoggerFactory.getLogger(Kafka2DingDingMain.class);

    public static void main(String[] args) {
        // 创建流处理的执行环境
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // kafka 地址
        String kafkaHost = parameterTool.get("Kafka_host", "172.24.215.5:9093");
        String kafkaTopic = parameterTool.get("Kafka_topic", "logstash_error_log");

        // 告警统一接口地址
        String dingdingPostURL = parameterTool.get("alert_post_url", "http://172.16.7.109:8355/alert/");
        // 窗口时间长度
        long windowTimeMinutes = parameterTool.getLong("window_time_minutes", 60L);

        if (!dingdingPostURL.endsWith("/")) {
            dingdingPostURL = dingdingPostURL + "/";
        }

        logger.info("Kafka_host : {}, kafka_topic: {} , dingding_post_url : {}, window_time_minutes : {}",
                kafkaHost, kafkaTopic, dingdingPostURL, windowTimeMinutes);

        // 配置KafKa
        //配置KafKa和Zookeeper的ip和端口
        // Properties
        final Properties props = new Properties();
        props.setProperty("bootstrap.servers", kafkaHost);
        props.setProperty("group.id", "logstash");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                kafkaTopic, new SimpleStringSchema(), props);
        // 从上次消费记录位置开始消费
        consumer.setStartFromEarliest();
        DataStream<String> dataStream = senv.addSource(consumer);

        // 从kafka中提取需要的key
        SingleOutputStreamOperator<LogMonitorBeans> reduce =
                dataStream.map(new MyMapFunction2())
                .keyBy(LogMonitorBeans::getType)
                .window(TumblingProcessingTimeWindows.of(Time.minutes(windowTimeMinutes)))
                .reduce(new MyReduceFunction());

        // test print
//        map.print("map");
//        reduce.print("reduce");
        // 发送 e-mail
        reduce.addSink(new SendAlertSink(dingdingPostURL,AlertTyre.EMAIL.getAlertTyre(), false));

        // 发送钉钉告警
        reduce.addSink(new SendAlertSink(dingdingPostURL, AlertTyre.DINGDING.getAlertTyre(),false));

        try {
            senv.execute("Send ErrorLog Alert");
        } catch (Exception e) {
            logger.error("An error occurred.", e);
        }
    }
}
