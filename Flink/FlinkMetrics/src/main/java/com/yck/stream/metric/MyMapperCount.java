package com.yck.stream.metric;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;


public class MyMapperCount extends RichMapFunction<Tuple2<String,Integer>, Tuple2<String,String>> {
    private transient Counter counter;
    @Override
    public void open(Configuration conf){
        this.counter = getRuntimeContext()
                .getMetricGroup()
                .counter("myCounter");
    }

    @Override
    public Tuple2<String, String> map(Tuple2<String, Integer> t2) throws Exception {
        this.counter.inc();
        Tuple2<String, String> re = new Tuple2<>();
        re.f0 = t2.f0;
        re.f1 = t2.f0 + "'s score is " +t2.f1+ ".";
        return re;
    }
}
