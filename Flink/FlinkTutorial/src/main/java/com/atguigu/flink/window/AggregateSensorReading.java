package com.atguigu.flink.window;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

public class AggregateSensorReading {
    public static void main(String[] args) throws Exception {
        // 随机生成温度传感器的值
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading(
                            "sensor_" + r.nextInt(4), 35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(1000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<Integer> aggregate = source.keyBy("name")
                .timeWindow(Time.seconds(10))
                .aggregate(new AggregateFunction<SensorReading, Integer, Integer>() {
                    @Override
                    public Integer createAccumulator() {
                        return 0;
                    }

                    @Override
                    public Integer add(SensorReading sensorReading, Integer integer) {
                        return integer + 1;
                    }

                    @Override
                    public Integer getResult(Integer integer) {
                        return integer;
                    }

                    @Override
                    public Integer merge(Integer integer, Integer acc1) {
                        return integer + acc1;
                    }
                });
        // 计算格式count
        source.print("随机生成");
        aggregate.print("计数");
        env.execute();

    }
}
