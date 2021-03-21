package com.atguigu.apitest.source;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SourceTest4_MySource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<SensorReading> sensorReadingDataStream = env.addSource(new SourceFunction<SensorReading>() {
            boolean is_running = true;
            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {

                Random random = new Random();
                Map<String,Double> sensorReadingMap = new HashMap<>();
                for(int i = 0;i<4;i++){
                    sensorReadingMap.put("sensor_"+i, 60+random.nextGaussian()*20);
                }

                while(is_running){
                    for(int i=0;i<4;i++){
                        Double t = sensorReadingMap.get("sensor_"+i)+random.nextGaussian();
                        sourceContext.collect(new SensorReading("sensor_"+i,System.currentTimeMillis(),t));
                        Thread.sleep(1000);
                    }
                }
            }

            @Override
            public void cancel() {
                is_running = false;
            }
        });

        sensorReadingDataStream.print();
        env.execute("");
    }

}


