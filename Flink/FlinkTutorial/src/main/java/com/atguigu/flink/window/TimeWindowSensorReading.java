package com.atguigu.flink.window;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

public class TimeWindowSensorReading {
    public static void main(String[] args) throws Exception {
        // 随机生成数据
        StreamExecutionEnvironment env =  StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading("sensor_" + r.nextInt(3),
                            35 + r.nextGaussian() * 3));
                    Thread.sleep(1000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<SensorReading> min = source.keyBy("name").timeWindow(Time.seconds(20)).minBy("tem");
        // 滚动 统计每10秒内的最低温度
        source.print("随机温度");
        min.print("十秒内最低问题");
        env.execute();
    }
}
