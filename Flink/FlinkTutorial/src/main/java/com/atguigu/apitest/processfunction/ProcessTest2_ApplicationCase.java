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

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：自定义一个方法，
 * 继承KeyedProcessFunction，里面通过timeService定义一个触发器，如果10秒内的状态温度一直在上升，
 * 那么则输出。
 * @date ：2021/3/14 下午12:13
 */


public class ProcessTest2_ApplicationCase {
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

        map.keyBy("name").process(new TempConsIncreWarning(10)).print();
        env.execute();
    }
    public static class TempConsIncreWarning extends KeyedProcessFunction<Tuple,SensorReading,String>{

        private ValueState<Double> lastTempState;
        private ValueState<Long> timerTsState;
        private Integer interval;

        @Override
        public void open(Configuration parameters) throws Exception {
            lastTempState = getRuntimeContext().getState(new ValueStateDescriptor<Double>("last-temp",Double.class,Double.MIN_VALUE));
            timerTsState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timer-ts",Long.class));
//            super.open(parameters);
        }

        @Override
        public void close() throws Exception {
            lastTempState.clear();
            timerTsState.clear();
            super.close();
        }

        public TempConsIncreWarning(Integer interval) {
            this.interval = interval;
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            // 定时器触发，输出报警信息
            out.collect("传感器"+ctx.getCurrentKey().getField(0)+"温度连续"+interval+"s上升");
            timerTsState.clear();
        }

        @Override
        public void processElement(SensorReading value, Context ctx, Collector<String> out) throws Exception {

            // 取出状态
            Double lastTemp = lastTempState.value();
            Long timerTs = timerTsState.value();

            // 如果温度上升并且没有定时器，则注册十秒后的定时器，开始等待
            if(value.getTem()>lastTemp && timerTs == null){
                Long ts = ctx.timerService().currentProcessingTime() + interval * 1000L;
                ctx.timerService().registerProcessingTimeTimer(ts);
                timerTsState.update(ts);
            }
            // 如果温度下降，那么删除定时器
            else if(value.getTem()<lastTemp && timerTs !=null){
                ctx.timerService().deleteEventTimeTimer(timerTs);
                timerTsState.clear();
            }

            // 更新温度状态
            lastTempState.update(value.getTem());
        }
    }
}
