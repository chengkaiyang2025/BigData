package com.atguigu.flink.transformer;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import scala.Int;

public class mapSensorReading {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 从文件中读取数据
        env.setParallelism(1);
        String filepath = "src/main/resources/sensor_reading_cold.csv";
        DataStream<String> dataStream = env.readTextFile(filepath);
        // 计算每行文本长度
        // 打印输出
        SingleOutputStreamOperator<Integer> map = dataStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String s) throws Exception {
                return s.length();
            }
        });
        map.print("map方法");
        env.execute();
    }

}
