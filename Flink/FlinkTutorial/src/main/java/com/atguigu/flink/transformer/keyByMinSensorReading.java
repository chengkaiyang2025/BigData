package com.atguigu.flink.transformer;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import sun.management.Sensor;

import java.util.stream.Stream;

public class keyByMinSensorReading {
    public static void main(String[] args) throws Exception {
        //读取文件数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file = "src/main/resources/sensor_reading.csv";
        DataStreamSource<String> stringDataStreamSource = env.readTextFile(file);

        //转为pojo类
        SingleOutputStreamOperator<String> map = stringDataStreamSource.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0],Long.valueOf(split[1]),Double.valueOf(split[2]));
            }
        }).keyBy("name").min("tem").map(new MapFunction<SensorReading, String>() {
            @Override
            public String map(SensorReading sensorReading) throws Exception {
                return sensorReading.getName()+"的当前最小温度为："+sensorReading.getTem();
            }
        });
        //计算最小值
        //打印输出
        map.print("计算传感器最小值");
        env.execute();
    }
}
