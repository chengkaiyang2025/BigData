package com.atguigu.apitest.transformer;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

public class connectSensorReading {
    public static void main(String[] args) throws Exception {
        // 读取两种传感器数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 将两种传感器数据源合并为一种。
        String file1 = "src/main/resources/sensor_reading.csv";
        String file2 = "src/main/resources/sensor_reading_cold.csv";
        DataStreamSource<String> sensor_1 = env.readTextFile(file1);
        DataStreamSource<String> sensor_2 = env.readTextFile(file2);

        ConnectedStreams<String, String> connect = sensor_1.connect(sensor_2);
        SingleOutputStreamOperator<SensorReading> map = connect.map(new CoMapFunction<String, String, SensorReading>() {
            @Override
            public SensorReading map1(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0],Double.valueOf(split[1]));

            }

            @Override
            public SensorReading map2(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0],Long.valueOf(split[1]),Double.valueOf(split[2]));

            }
        });

        // 输出
        map.print("connect");
        env.execute();
    }
}
