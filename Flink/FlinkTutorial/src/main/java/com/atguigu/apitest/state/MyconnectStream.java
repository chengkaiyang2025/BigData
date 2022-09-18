package com.atguigu.day03;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjl on 2022/9/18
 */
public class MyconnectStream {
    private static  String data = "";

    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = environment.fromCollection(Arrays.asList("1", "2", "3"));
        DataStreamSource<Integer> source = environment.fromCollection(Arrays.asList(1, 2, 3,4));
        streamSource.connect(source).process(new CoProcessFunction<String, Integer, Object>() {

//            private String data = "";

            @Override
            public void processElement1(String s, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                collector.collect(data+"####"+s+"***");
            }

            @Override
            public void processElement2(Integer integer, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                data = integer*3+"";
                collector.collect(integer*3);
            }
        }).print("dddd");
        environment.execute();
    }
}
