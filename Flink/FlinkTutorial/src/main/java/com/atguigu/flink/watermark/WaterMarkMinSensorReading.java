package com.atguigu.flink.watermark;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/8 下午9:11
 */


public class WaterMarkMinSensorReading {
    public static void main(String[] args) throws Exception {
        //从socket文本流中读取数据
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // 设置配置Event时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> source = env.socketTextStream("localhost", 7777);
        // 转换为pojo类，定义Event时间
        DataStream<SensorReading> streamOperator = source.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(SensorReading sensorReading) {
                return sensorReading.getTimeStamp()*1000L;
            }
        });

        // 定义侧输出流
        OutputTag<SensorReading> outputTag = new OutputTag<SensorReading>("late"){};

        //基于事件时间的开窗聚合，统计15秒之内的最小温度。

        SingleOutputStreamOperator<SensorReading> minBy = streamOperator.keyBy("name")
                .timeWindow(Time.seconds(15))
                .allowedLateness(Time.minutes(1))
                .sideOutputLateData(outputTag)
                .minBy("tem");
        source.print("s");
        minBy.print("最小温度");
        minBy.getSideOutput(outputTag).print("迟到");
        env.execute();
    }
}
