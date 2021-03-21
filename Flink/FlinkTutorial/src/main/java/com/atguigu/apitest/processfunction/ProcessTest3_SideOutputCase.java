package com.atguigu.apitest.processfunction;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/14 下午12:49
 */


public class ProcessTest3_SideOutputCase {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<SensorReading> source = env.socketTextStream("localhost", 7777).map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        });

        OutputTag<SensorReading> lowTempTag = new OutputTag<SensorReading>("lowTemp"){

        };
        SingleOutputStreamOperator<Object> highTempStream = source.keyBy("name").process(new KeyedProcessFunction<Tuple, SensorReading, Object>() {
            @Override
            public void processElement(SensorReading value, Context ctx, Collector<Object> out) throws Exception {
                if (value.getTem() > 30) {
                    out.collect(value);
                } else {
                    ctx.output(lowTempTag, value);
                }
            }
        });
        highTempStream.print("high");
        highTempStream.getSideOutput(lowTempTag).print("low");
        env.execute();


    }
}
