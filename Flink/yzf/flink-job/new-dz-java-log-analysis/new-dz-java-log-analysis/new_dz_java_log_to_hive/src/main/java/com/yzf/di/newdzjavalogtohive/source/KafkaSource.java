package com.yzf.di.newdzjavalogtohive.source;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import com.yzf.di.newdzjavalogtohive.serial.JavaLogBeanDeserializationSchema;
import com.yzf.di.newdzjavalogtohive.util.MyPropertiesUtil;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaSource {
    private static final List<String> topicList = new ArrayList<>();
    private String source_kafka_broker;
    private String source_kafka_topic_list;
    private String source_kafka_groupId;
    private String source_kafka_auto_offset_reset;
    private String source_kafka_parallelism;

    /** 将kafka配置的前缀与配置拼接
     * @param mp
     * @param prefix kafka配置的前缀
     */
    public void setUpProperties(MyPropertiesUtil mp,String prefix){
        source_kafka_broker = mp.get( prefix + "broker");
        source_kafka_groupId = mp.get( prefix + "groupId");
        source_kafka_auto_offset_reset = mp.get( prefix + "auto.offset.reset");
        source_kafka_topic_list = mp.get( prefix + "topic.list");
        source_kafka_parallelism = mp.get( prefix + "parallelism");
    }

    /**
     * @param env
     * @param mp
     * @param prefix kafka配置的前缀
     * @return
     */
    public DataStream<JavaLogBean> source(StreamExecutionEnvironment env,MyPropertiesUtil mp,String prefix) {
        Properties p = new Properties();
        setUpProperties(mp, prefix);
        p.put("bootstrap.servers", source_kafka_broker);
        p.put("group.id", source_kafka_groupId);
        p.put("auto.offset.reset", source_kafka_auto_offset_reset);

        for (String topic : source_kafka_topic_list.split(",")) {
            topicList.add(topic);
        }
        
        DataStreamSource<JavaLogBean> source = env.addSource(new FlinkKafkaConsumer<JavaLogBean>(
                topicList,
                new JavaLogBeanDeserializationSchema(),
                p
                // 测试从最新取
                )
        ).setParallelism(Integer.parseInt(
                source_kafka_parallelism
        ));

        return source;
    }
}
