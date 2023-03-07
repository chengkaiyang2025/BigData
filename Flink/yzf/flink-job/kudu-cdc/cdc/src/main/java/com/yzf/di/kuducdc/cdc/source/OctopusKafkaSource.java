package com.yzf.di.kuducdc.cdc.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 接入线上测试
 * 测试环境与线上环境都会做检查点，因此Flink consumer的offset存储在检查点里面，在做检查点的时候提交偏移量。
 * 因此这里不用做过多配置，配置了也没有用。
 * https://ci.apache.org/projects/flink/flink-docs-release-1.13/docs/connectors/datastream/kafka/
 */
public class OctopusKafkaSource {
    public DataStreamSource<ObjectNode> source(StreamExecutionEnvironment env, String bootStrap){

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootStrap);
        properties.setProperty("group.id", "kudu-cdc");

        FlinkKafkaConsumer<ObjectNode> consumer = new FlinkKafkaConsumer<>(
                "octopus_fetchData_topic",
                new JSONKeyValueDeserializationSchema(true),
                properties);
//
//
//        Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
//        specificStartOffsets.put(new KafkaTopicPartition("octopus_fetchData_topic", 0), 900000L);
//        specificStartOffsets.put(new KafkaTopicPartition("octopus_fetchData_topic", 1), 900000L);
//        specificStartOffsets.put(new KafkaTopicPartition("octopus_fetchData_topic", 2), 900000L);
//
//        consumer.setStartFromSpecificOffsets(specificStartOffsets);
//

//        DataStream<ObjectNode> source = new AutoGenerateSource().source(env);
        DataStreamSource<ObjectNode> source = env
                .addSource(consumer)
                .setParallelism(3);
        return source;
    }
}
