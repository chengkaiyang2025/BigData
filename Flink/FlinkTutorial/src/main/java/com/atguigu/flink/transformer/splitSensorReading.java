package com.atguigu.flink.transformer;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Collections;

public class splitSensorReading {
    public static void main(String[] args) throws Exception {
        // 读取数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file = "src/main/resources/sensor_reading.csv";
        DataStreamSource<String> streamSource = env.readTextFile(file);
        // 按照30度进行分流
        SplitStream<SensorReading> split = streamSource.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        }).keyBy("name").split(new OutputSelector<SensorReading>() {
            @Override
            public Iterable<String> select(SensorReading sensorReading) {
                if (sensorReading.getTem() > 30) {
                    return Collections.singletonList("high");
                } else return Collections.singletonList("low");
            }
        });
        //输出流：
        DataStream<SensorReading> low = split.select("low");
        DataStream<SensorReading> high = split.select("high");
        // 输出结果

        low.print("低温流：");
        high.print("高温流：");
        env.execute();
    }
}
