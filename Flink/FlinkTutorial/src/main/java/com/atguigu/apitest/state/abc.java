package com.atguigu.apitest.state;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class abc {
    private static String date = "";
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        DataStreamSource<String> streamSource = environment.fromCollection(Arrays.asList("1,2,3"));
        DataStreamSource<Integer> source = environment.fromCollection(Arrays.asList(1,2,3,4));
        streamSource.connect(source).process(new CoProcessFunction<String, Integer, Object>() {
            @Override
            public void processElement1(String s, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                collector.collect(date+"####"+s+"***");
            }

            @Override
            public void processElement2(Integer integer, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                date = integer*3+"";
                collector.collect(integer*3);
            }
        }).print("ddd");
        environment.execute();
    }
}
