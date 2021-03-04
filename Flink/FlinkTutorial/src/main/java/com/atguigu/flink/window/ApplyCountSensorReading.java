package com.atguigu.flink.window;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.util.Random;

public class ApplyCountSensorReading {
    public static void main(String[] args) throws Exception {
        // 使用全窗口函数计算个数/平均温度
        StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading(
                            "sensor_" + r.nextInt(4),
                            35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(1000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        source.print("随机生成");
        source.keyBy("name")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(15)))
                .apply(new WindowFunction<SensorReading, Tuple3<String,Long,Integer>, Tuple, TimeWindow>() {

                })


        env.execute();
    }
}
