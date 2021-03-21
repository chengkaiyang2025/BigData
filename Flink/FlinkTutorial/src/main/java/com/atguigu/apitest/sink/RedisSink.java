package com.atguigu.apitest.sink;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

public class RedisSink {
    public static void main(String[] args) throws Exception {
        //生成数据源
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            Random r = new Random();
            boolean isRunning = true;
            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning){
                    sourceContext.collect(new SensorReading("sensor_"+r.nextInt(4),
                            35+r.nextGaussian()));
                    Thread.sleep(2000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        //sink到redis中
        source.print();
        env.execute();
    }
}
