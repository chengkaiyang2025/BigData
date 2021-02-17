package com.atguigu.flink.stream;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;
import java.util.zip.DataFormatException;

public class SourceTest3_Kafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        Properties p = new Properties();
         
        p.put("bootstrap.servers","localhost:9092");
        p.put("group.id","consumer-group");
        p.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        p.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        p.put("auto.offset.reset","latest");
        DataStream<String> kafkaDataSource = env.addSource(new FlinkKafkaConsumer<String>("sensor",new SimpleStringSchema(),p));
        kafkaDataSource.print("Kafka sensor");
        env.execute();
    }
}
