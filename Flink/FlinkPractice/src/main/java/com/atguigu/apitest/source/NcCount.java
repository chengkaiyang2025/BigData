package com.atguigu.apitest.source;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.util.Collector;

/**
 * Created by hjl on 2022/8/19
 */
public class NcCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> stringDataStreamSource = executionEnvironment.socketTextStream("localhost", 9999, "\n");
        SingleOutputStreamOperator<Tuple2<String, Long>> streamOperator = stringDataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Long>> collector) throws Exception {
                String[] splits = s.split("\\s");
                for (String word : splits) {
                    collector.collect(Tuple2.of(word, 1l));
                }
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Long>> word = streamOperator.keyBy(new KeySelector<Tuple2<String, Long>, Object>() {

            @Override
            public Object getKey(Tuple2<String, Long> stringLongTuple2) throws Exception {
                return stringLongTuple2.f0;
            }
        }).sum(1);
//        StreamingFileSink<Tuple2<String, Long>> sink = StreamingFileSink.<Tuple2<String, Long>>forRowFormat(new Path("./output"), new SimpleStringEncoder<>()).build();
//        word.addSink(sink);
        word.print();
        executionEnvironment.execute("deal nc listener");
    }
}
