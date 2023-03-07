package com.atguigu.apitest.processfunction;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：最基础的KeydProcessFucntion,通过实现processElement方法进行计算
 * 同时通过context上下文，获得TimeService，在当前的processTime、水印线等相关信息。
 * @date ：2021/3/14 上午11:07
 */


public class ProcessTest1_KeyedProcess {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> source = env.socketTextStream("localhost", 7777);

        SingleOutputStreamOperator<SensorReading> map = source.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        });
        map.keyBy("name")
                .process(new MyProcess())
                .print();
        env.execute();
    }

    /**
     * 定义一个MyProcess,使用timeService中的registerProcessingTime方法，注册3秒和5秒后触发新方法。
     *
     */
    public static class MyProcess extends KeyedProcessFunction<Tuple, SensorReading, Integer>{
        ValueState<Long> tsTimeState;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        @Override
        public void open(Configuration parameters) throws Exception {
            tsTimeState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("ts-timer",Long.class));
        }

        @Override
        public void close() throws Exception {
            tsTimeState.clear();
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Integer> out) throws Exception {
            System.out.println(sdf.format(timestamp)+" 定时器触发");
            System.out.println(ctx.getCurrentKey());
            System.out.println(ctx.timeDomain());
        }

        @Override
        public void processElement(SensorReading value, Context ctx, Collector<Integer> out) throws Exception {
            System.out.println("当前处理的key为："+ctx.getCurrentKey());
            System.out.println("当前时间戳"+sdf.format(new Date(ctx.timerService().currentProcessingTime())));
//            System.out.println("水印线:"+sdf.format(ctx.timerService().currentWatermark()));
//            tsTimeState.update(ctx.timerService().currentProcessingTime() + 1000L);
            ctx.timerService().registerProcessingTimeTimer(( ctx.timerService().currentProcessingTime()+ 3000L));
            ctx.timerService().registerProcessingTimeTimer(( ctx.timerService().currentProcessingTime()+ 5000L));

//            System.out.println(ctx.timerService().);
            out.collect(value.getName().length());
        }
    }
}
