package com.atguigu.apitest.sink;

import com.atguigu.apitest.beans.SensorReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Random;
public class KafkaSink {
    public static void main(String[] args) throws Exception {
        // 自定义数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<String> map = env.addSource(new SourceFunction<Object>() {
            private boolean isRun = true;

            @Override
            public void run(SourceContext<Object> sourceContext) throws Exception {
                Random r = new Random();
                while (isRun) {
                    sourceContext.collect(new SensorReading(
                            "sensor_" + r.nextInt(4),
                            Double.valueOf(35 + r.nextGaussian())
                    ));
                    Thread.sleep(2000);
                }
            }

            @Override
            public void cancel() {
                this.isRun = false;
            }
        }).map(new MapFunction<Object, String>() {
            @Override
            public String map(Object o) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                String json = mapper.writeValueAsString(o);

                return json ;
            }
        });
        // 写入kafka
        map.print();
        map.addSink(new FlinkKafkaProducer<String>("172.24.10.5:9092","nginx_log_anticrawler_formatter",new SimpleStringSchema()));
        env.execute();
    }
}
