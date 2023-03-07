package com.yck.stream.metric;

import com.codahale.metrics.SlidingWindowReservoir;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.Histogram;

/**
 *
 */
public class MyMapperHistogram extends RichMapFunction<Tuple2<String,Integer>, Tuple2<String,Integer>> {
    private transient Histogram histogram;

    @Override
    public void open(Configuration conf) throws Exception {
        com.codahale.metrics.Histogram dropwizardHistogram =
                new com.codahale.metrics.Histogram(new SlidingWindowReservoir(500));
        this.histogram = getRuntimeContext()
                .getMetricGroup()
                .histogram("histogram",new DropwizardHistogramWrapper(dropwizardHistogram));
    }

    @Override
    public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
        this.histogram.update(value.f1);
        return value;
    }
}
