package com.atguigu.flink.transformer;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class keyByMaxSensorReading {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 1、读取文件
        String file = "src/main/resources/sensor_reading.csv";
        DataStreamSource<String> stringDataStreamSource = env.readTextFile(file);
        // 2、转为pojo类。
        SingleOutputStreamOperator<SensorReading> map = stringDataStreamSource.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0],Long.valueOf(split[1]),Double.valueOf(split[2]));
            }
        });
        // 3、按照传感器的编号获得当前最大的温度。
        SingleOutputStreamOperator<String> map1 = map.keyBy("name").max("tem").map(new MapFunction<SensorReading, String>() {
            @Override
            public String map(SensorReading sensorReading) throws Exception {
                return sensorReading.getName()+"的当前最大温度："+sensorReading.getTem();
            }
        });
        // 4、打印
        map1.print("计算当前传感器温度最大值");
        env.execute();

    }
}
