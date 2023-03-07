package com.yck.stream.metric;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Gauge;

/**
 * Count that sum of value > 500
 */
public class MyMapperGauge extends RichMapFunction<Tuple2<String,Integer>, Tuple2<String,Integer>> {
    private transient int v = 0;
    @Override
    public void open(Configuration conf) throws Exception {
//        super.open(parameters);
        getRuntimeContext().getMetricGroup()
                .gauge("UpTo500", new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return v;
                    }
                });
    }

    @Override
    public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
        if(value.f1>500){
            v++;
        }
        return value;
    }
}
