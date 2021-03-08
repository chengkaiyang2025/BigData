package com.atguigu.flink.transformer;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class flatMapSensorReading {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 调整并行度，读取数据源
        env.setParallelism(1);
        String file = "src/main/resources/sensor_reading_cold.csv";
        DataStreamSource<String> dataStreamSource = env.readTextFile(file);

        // 按照，分词
        SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = dataStreamSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] splits= s.split(",");
                for(String split:splits){
                    collector.collect(split);
                }
            }
        });
        // 打印
        stringSingleOutputStreamOperator.print("flatMap");
        env.execute();
    }
}
