package com.atguigu.apitest.window;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichAggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

/**
 * 窗口函数中，aggregate的性能要比apply要好很多
 * https://ci.apache.org/projects/flink/flink-docs-release-1.12/dev/stream/operators/windows.html#aggregatefunction
 */
public class AggregateAvgSensorReading {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> ctx) throws Exception {
                while (isRunning) {
                    ctx.collect(new SensorReading(
                            "sensor_" + r.nextInt(3), 35 + r.nextGaussian() * 10
                    ));
                    Thread.sleep(1000L);
                }

            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        // 使用RichAggregateFunction获得 当前的key
        SingleOutputStreamOperator<Tuple2<String,Double>> aggregate = source.keyBy("name").timeWindow(Time.seconds(5))
                .aggregate(new AggregateFunction<SensorReading, Tuple3<String,Double, Integer>, Tuple2<String,Double>>() {

                    @Override
                    public Tuple3<String,Double, Integer> createAccumulator() {

                        return new Tuple3<>("",Double.valueOf(0L),0);
                    }

                    @Override
                    public Tuple3<String,Double, Integer> add(SensorReading value, Tuple3<String,Double, Integer> accumulator) {
                        return new Tuple3<>(value.getName(),value.getTem()+accumulator.f1,accumulator.f2+1);

                    }

                    @Override
                    public Tuple2<String,Double> getResult(Tuple3<String, Double, Integer> a) {

                        return new Tuple2<>(a.f0,a.f1/a.f2);
                    }

                    @Override
                    public Tuple3<String,Double, Integer> merge(Tuple3<String,Double, Integer> a, Tuple3<String,Double, Integer> b) {
                        return new Tuple3<>(a.f0,a.f1+b.f1,a.f2+b.f2);
                    }
                });
        source.print("SOURCE---------");
        aggregate.print("AVG---------");
        env.execute();

    }
}
