package com.atguigu.flink.stream;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceTest2_LocalFIle {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        String filePath = "src/main/resources/sersor_reading.csv";
        DataStream<String> input = env.readTextFile(filePath);
        DataStream<SensorReading> sensorReadingDataStream = input.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] re = s.split(",");
                Long t = Long.valueOf(re[1]);
                String name = re[0];
                double tem = Double.parseDouble(re[2]);
                return new SensorReading(name,t,tem);
            }
        });
        sensorReadingDataStream.print("sensor_reading");
        env.execute("从文件中读取");
    }
}
