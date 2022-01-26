package com.yck.stream;

import com.yck.stream.metric.MyMapperCount;
import com.yck.stream.metric.MyMapperGauge;
import com.yck.stream.metric.MyMapperHistogram;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 编写一个apply方法，在窗口中，不断申请一个列表，直到将内存申请完。
 */
public class MetricOOMStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> source = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private Boolean isCancel = false;
            private final Random r = new Random();
            @Override
            public void run(SourceContext<Tuple2<String, Integer>> sourceContext) throws Exception {
                while (!isCancel){
//                    Thread.sleep(1);
                    int i = r.nextInt(5);
                    String key = "";
                    switch (i){
                        case 0: key = "Tom"; break;
                        case 1: key = "Jerry"; break;
                        case 2: key = "Jack"; break;
                        case 3: key = "Louie"; break;
                        case 4: key = "William"; break;
                    }
                    Tuple2<String, Integer> re = new Tuple2<>();
                    re.f0 = key;re.f1 = r.nextInt();
                    sourceContext.collect(re);
                }
            }

            @Override
            public void cancel() {
                isCancel = true;
            }
        });
        SingleOutputStreamOperator<Tuple2<String, String>> sum = source.map(new MyMapperHistogram())
                .keyBy(k -> k.f0)
                // 修改开窗时间，引发不同的gc
                // TaskManager taskmanager.memory.process.size: 5g
                // 经试验，15秒会gc时间过长，导致 TaskManager 挂掉。
                // 10秒会导致old gc导致10秒左右的stop the world，比较卡顿
                // 5秒会规律的old gc，也就1秒左右的stop the world
                // 1秒不会引起old gc
                .window(TumblingProcessingTimeWindows.of(Time.milliseconds(100)))
                .apply(new WindowFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<Tuple2<String, Integer>> iterable, Collector<Tuple2<String, Integer>> collector) throws Exception {
                        List<Tuple2<String,Integer>> a = new ArrayList<>();
                        Iterator<Tuple2<String, Integer>> iterator = iterable.iterator();
                        while (iterator.hasNext()){
                            a.add(iterator.next());
                        }
                        System.out.println(a.size());
                        for (int i = 0; i < a.size(); i++) {
                            Tuple2<String, Integer> str = a.get(i);
                            str.f1 = str.f1+1;
                            // 这一步会生成很多String constant，促使常量池变大
                            str.f0 = str.f0 + str.f1;
                            a.set(i,str);
                        }
                        Random r = new Random();
                        collector.collect(a.get(r.nextInt(a.size())));
                    }
                })
                .setParallelism(2)
                .map(new MyMapperGauge()).map(new MyMapperCount());
        sum.print();
        env.execute();
    }
}
