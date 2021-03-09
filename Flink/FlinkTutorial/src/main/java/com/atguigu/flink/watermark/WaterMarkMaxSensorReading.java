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
import sun.management.Sensor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：每10秒开滚动窗口计算最小温度，2秒水印线，60秒的允许延时，迟到测输出流
 * @date ：2021/3/9 上午10:35
 */


public class WaterMarkMaxSensorReading {
    public static void main(String[] args) throws Exception {
        // 通过nc -lk 7777 读取数据源。
        // 数据样例为sensor_1,2021-03-07 10:11:10,40
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 初始化
        DataStream<SensorReading> source = env.socketTextStream("localhost", 7777)
                .map(new MapFunction<String, SensorReading>() {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    @Override
                    public SensorReading map(String s) throws Exception {
                        String[] split = s.split(",");
                        String sensor_name = split[0];
                        Long ts = sdf.parse(split[1]).getTime();
                        Double tem = Double.valueOf(split[2]);
                        return new SensorReading(sensor_name, ts, tem);
                    }
                }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
                    @Override
                    public long extractTimestamp(SensorReading sensorReading) {
                        return sensorReading.getTimeStamp();
                    }
                });
        OutputTag<SensorReading> late = new OutputTag<SensorReading>("late"){};

        // 计算10秒滚动窗口最小温度
        SingleOutputStreamOperator<SensorReading> minBy = source.keyBy("name")
                .timeWindow(Time.seconds(10))
                .allowedLateness(Time.seconds(60))
                .sideOutputLateData(late)
                .maxBy("tem");


//        source.print("原始数据流");
        minBy.print("10秒内最小温度:");
        minBy.getSideOutput(late).print("迟到数据");
        env.execute();
    }
}
