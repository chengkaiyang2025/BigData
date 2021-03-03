package com.atguigu.flink.window;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import sun.management.Sensor;

import java.util.Random;

public class CountWindowSensorReading {
    public static void main(String[] args) throws Exception {
        // 每5个元素统计最小值
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> sensor_1 = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading(
                            "sensor_1",
                            35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(2000);

                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<SensorReading> min = sensor_1.keyBy("name").countWindow(5,1).minBy("tem");
        sensor_1.print("随机产生");
        min.print("每5个");
        env.execute();
    }
}
