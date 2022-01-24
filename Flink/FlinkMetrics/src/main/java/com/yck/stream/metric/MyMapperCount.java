package com.yck.stream.metric;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class MyMapperCount extends RichMapFunction<Tuple2<String,Integer>, Tuple2<String,Integer>> {
    @Override
    public Tuple2<String, Integer> map(Tuple2<String, Integer> t2) throws Exception {
        return t2;
    }
}
