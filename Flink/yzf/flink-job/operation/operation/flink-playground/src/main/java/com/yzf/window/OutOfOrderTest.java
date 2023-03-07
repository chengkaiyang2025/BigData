package com.yzf.window;

import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class OutOfOrderTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
//        DataStreamSource<String> source = env.socketTextStream("localhost", 7777);
        DataStreamSource<String> source = env.addSource(new SourceFunction<String>() {

            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                ctx.collect("0,0");
                ctx.collect("0,55");
                ctx.collect("1,5");
                ctx.collect("0,44");
                ctx.collect("1,10");
                ctx.collect("1,50");
                ctx.collect("2,4");
                ctx.collect("1,33");
                ctx.collect("2,10");
            }

            @Override
            public void cancel() {

            }
        });
        SingleOutputStreamOperator<Tuple3<Date, Integer, String>> add = source.map(new MapFunction<String, Tuple3<Date, Integer, String>>() {
            @Override
            public Tuple3<Date, Integer, String> map(String value) throws Exception {
                String[] split = value.split(",");
                int minute = Integer.parseInt(split[0]);
                int second = Integer.parseInt(split[1]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(2021, 9, 26, 16, minute, second);
                System.out.println(value);

                return new Tuple3<>(calendar.getTime(), 1, "ADD");
            }
        }).assignTimestampsAndWatermarks(WatermarkStrategy
                .<Tuple3<Date, Integer, String>>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner((event, timestamp) -> event.f0.getTime()))
                .keyBy(new KeySelector<Tuple3<Date, Integer, String>, Object>() {
                    @Override
                    public Object getKey(Tuple3<Date, Integer, String> value) throws Exception {
                        return value.f2;
                    }
                }).window(TumblingEventTimeWindows.of(Time.minutes(1))).allowedLateness(Time.minutes(1))
                .apply(new WindowFunction<Tuple3<Date, Integer, String>, Tuple3<Date, Integer, String>, Object, TimeWindow>() {
                    @Override
                    public void apply(Object o, TimeWindow window, Iterable<Tuple3<Date, Integer, String>> input, Collector<Tuple3<Date, Integer, String>> out) throws Exception {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm.ss");
                        System.out.println("------------");

//                        System.out.println("窗口开始：" + sdf.format(window.getStart()));

                        Iterator<Tuple3<Date, Integer, String>> iterator = input.iterator();
                        List<Tuple3<Date, Integer, String>> list = IteratorUtils.toList(iterator);
                        list.sort(new Comparator<Tuple3<Date, Integer, String>>() {
                            @Override
                            public int compare(Tuple3<Date, Integer, String> o1, Tuple3<Date, Integer, String> o2) {
                                return Long.compare(o1.f0.getTime(), o2.f0.getTime());
                            }
                        });
                        for (Tuple3<Date, Integer, String> dateIntegerStringTuple3 : list) {
                            out.collect(dateIntegerStringTuple3);
                        }
//                        System.out.println("窗口结束：" + sdf.format(window.getEnd()));


                    }

                });

        SingleOutputStreamOperator<Tuple3<Date, Integer, String>> re = add.map(new MapFunction<Tuple3<Date, Integer, String>, Tuple3<Date, Integer, String>>() {
            @Override
            public Tuple3<Date, Integer, String> map(Tuple3<Date, Integer, String> value) throws Exception {
                return new Tuple3<>(value.f0,value.f1,value.f2+":after sort");
            }
        });
        re.print();
        env.execute();
    }
}
