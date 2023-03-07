package com.atguigu.apitest.window;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApplyAvgCountWindowSensorReading {
    public static void main(String[] args) throws Exception {
        //随机生成sensorreading，统计一定各式的分布
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new SensorReading(
                            "sensor" + r.nextInt(4),
                            35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(200);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Double>> count = source.keyBy("name")
                .countWindow(10)
                .apply(new WindowFunction<SensorReading, Tuple2<String,Double>, Tuple, GlobalWindow>() {
                    @Override
                    public void apply(Tuple tuple, GlobalWindow globalWindow, Iterable<SensorReading> iterable, Collector<Tuple2<String,Double>> collector) throws Exception {
                        String key = tuple.getField(0).toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                        String time = sdf.format(new Date());
                        List<SensorReading> list = IteratorUtils.toList(iterable.iterator());
                        Double sum = Double.valueOf(0);
                        for (SensorReading sensorReading : list) {
                             sum += sensorReading.getTem();
                        }
                        Double v = Double.valueOf(sum / list.size());
                        collector.collect(new Tuple2<>(key,v));
                    }
                });
        count.print("平均温度：");
        source.print("随机生成");
        env.execute();
    }
}
