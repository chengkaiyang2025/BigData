package com.yzf.source;

import com.yzf.config.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从Kafka中消费nginx日志
 * @date ：2021/1/27 下午5:56
 */


public class NginxLogKafkaSource {

    public static DataStream<ObjectNode> getKafkaSource(StreamExecutionEnvironment env) {
        DataStreamSource<ObjectNode> k1 = env.addSource(
                new FlinkKafkaConsumer<>(
                        Configuration.NGINX_TOPIC,
                        new JSONKeyValueDeserializationSchema(false),
                        Configuration.PROPERTIES)
        );

        return k1;
    }
}
