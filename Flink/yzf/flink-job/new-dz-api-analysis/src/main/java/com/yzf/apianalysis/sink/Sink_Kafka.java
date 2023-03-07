package com.yzf.apianalysis.sink;

import com.yzf.apianalysis.beans.ApiInfo;
import com.yzf.apianalysis.source.DemoApiSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/23 17:02
 * @description：
 */
public class Sink_Kafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> inputStream = env.addSource(new DemoApiSource());


//        inputStream.print();
        inputStream.addSink(new FlinkKafkaProducer<String>("172.26.9.81:9092", "nginx_log", new SimpleStringSchema()));


        env.execute("UserDefineSource to Kafka");
    }

}

