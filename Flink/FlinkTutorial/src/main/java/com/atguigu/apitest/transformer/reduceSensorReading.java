package com.atguigu.apitest.transformer;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class reduceSensorReading {
    public static void main(String[] args) throws Exception {
        // 获得数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file = "src/main/resources/sensor_reading_cold.csv";
        DataStreamSource<String> stringDataStreamSource = env.readTextFile(file);
        // 比较获得当前最大值
        SingleOutputStreamOperator<SensorReading> name = stringDataStreamSource.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        }).keyBy("name").reduce(new ReduceFunction<SensorReading>() {
            @Override
            public SensorReading reduce(SensorReading sensorReading, SensorReading t1) throws Exception {
                return new SensorReading(sensorReading.getName(),sensorReading.getTimeStamp(),
                Math.max(sensorReading.getTem(),t1.getTem()));
            }
        });
        // 输出结果
        name.print("reduce");
        env.execute();
    }
}
