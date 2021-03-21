package com.atguigu.apitest.window;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ApplyAvgTimeWindowSensorReading {
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
        SingleOutputStreamOperator<Tuple3<String, String, Double>> name = source.keyBy("name")
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .apply(new WindowFunction<SensorReading, Tuple3<String, String, Double>, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<SensorReading> iterable, Collector<Tuple3<String, String, Double>> collector) throws Exception {
                        String s = tuple.getField(0).toString();
                        List<SensorReading> list = IteratorUtils.toList(iterable.iterator());
                        Double sum = Double.valueOf(0);
                        for (SensorReading sensorReading : list) {
                            sum = sum + sensorReading.getTem();
                        }
                        double avg = sum / list.size();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                        long end = timeWindow.getEnd();
                        String t = sdf.format(new Date(end));

//                        System.out.println(avg);
                        collector.collect(new Tuple3<>(s, t, avg));
                    }
                });

        name.print("平均数");
        env.execute();
    }
}




