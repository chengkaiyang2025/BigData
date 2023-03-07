package com.yzf.di.newdzlogsinkhive.source;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.serial.LogBeanDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;

import java.util.Properties;

public class KafkaSource {
    public DataStream<LogBean> source(StreamExecutionEnvironment env,String bootstrapSource,int parallelism){
        String groupId = "nginx-analysis-stream-newdzlogsinkhive";
        Properties p = new Properties();
        p.put("bootstrap.servers",bootstrapSource);
        p.put("group.id",groupId);
        p.put("auto.offset.reset","earliest");
        // 5MB

        DataStream<LogBean> kafkaSource = env.addSource(
                new FlinkKafkaConsumer<>(
                        "safe_interface_log",
                        new LogBeanDeserializationSchema(),
                        // 线下测试！ 从最开始
                        p).setStartFromLatest()
        ).setParallelism(parallelism).uid("source-1");
        return kafkaSource;
    }
}
