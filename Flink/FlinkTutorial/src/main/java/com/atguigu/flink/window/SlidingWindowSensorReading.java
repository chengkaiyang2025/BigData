package com.atguigu.flink.window;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

public class SlidingWindowSensorReading {
    public static void main(String[] args) throws Exception {
        // 随机2秒生成一个传感器数据
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> sensor_1 = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading(
                            "sensor_1", 35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(2000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<SensorReading> min = sensor_1.keyBy("name").timeWindow(Time.seconds(10), Time.seconds(2)).minBy("tem");
        // 每1秒计算近10秒的传感器最低问题
        sensor_1.print("随机产生");
        min.print("------温度最低为------");
        env.execute();
    }
}
