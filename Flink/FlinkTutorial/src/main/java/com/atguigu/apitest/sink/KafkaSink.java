package com.atguigu.apitest.sink;

import com.atguigu.apitest.beans.SensorReading;
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
                            "sensor_" + r.nextInt(4), 35 + r.nextGaussian()
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
                return o.toString();
            }
        });
        // 写入kafka
        map.print();
        map.addSink(new FlinkKafkaProducer<String>("172.38.0.138:9092","sensor",new SimpleStringSchema()));
        env.execute();
    }
}
