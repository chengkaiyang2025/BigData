package com.atguigu.flink.transformer;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class unionSensorReading {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file1 = "src/main/resources/sensor_reading.csv";
        String file2 = "src/main/resources/sensor_reading_cold.csv";

        DataStreamSource<String> sensor_1 = env.readTextFile(file1);
        DataStreamSource<String> sensor_2 = env.readTextFile(file2);

        DataStream<String> union = sensor_1.union(sensor_2);
        union.print("union");
        env.execute();
    }
}
