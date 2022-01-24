package com.yck.stream;

import com.yck.stream.metric.MyMapperCount;
import com.yck.stream.metric.MyMapperGauge;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

public class MetricStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> source = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private Boolean isCancel = false;
            private Random r = new Random();
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
        SingleOutputStreamOperator<Tuple2<String, String>> sum = source.keyBy(k -> k.f0)
                .countWindow(10).sum(1).setParallelism(2)
                .map(new MyMapperGauge()).map(new MyMapperCount());
        sum.print();
        env.execute();
    }
}
