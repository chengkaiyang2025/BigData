package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 从文件 sensor.txt 中读取数据
 * 在流中设置指定时间字段，并设置2秒的水位线。
 * 然后转为流表。
 */
public class TableTest5_EventTimeDefinedInStream {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 设置Event时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        String file = "src/main/resources/sensor.txt";
        DataStream<String> source = env.readTextFile(file);
        SingleOutputStreamOperator<SensorReading> map = source.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(
                        split[0],
                        Long.valueOf(split[1]),
                        Double.valueOf(split[2])
                );
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(SensorReading element) {
                return element.getTimeStamp()*1000;
            }
        });
    }
}