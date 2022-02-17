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
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 由于sum实现的是reduce方法，所以不会引发old gc，只是引发young gc，而且时间非常短
 */
public class MetricStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> source = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private Boolean isCancel = false;
            private final Random r = new Random();
            @Override
            public void run(SourceContext<Tuple2<String, Integer>> sourceContext) throws Exception {
                while (!isCancel){
                    Thread.sleep(10);
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
                    re.f0 = key;re.f1 = r.nextInt(100);
                    sourceContext.collect(re);
                }
            }

            @Override
            public void cancel() {
                isCancel = true;
            }
        });
        SingleOutputStreamOperator<Tuple2<String, String>> sum = source.map(new MyMapperHistogram())
                .keyBy(k -> k.f0).window(TumblingProcessingTimeWindows.of(Time.minutes(1))).sum(1).setParallelism(2)
                .map(new MyMapperGauge()).map(new MyMapperCount());
        sum.print();
        env.execute();
    }
}
