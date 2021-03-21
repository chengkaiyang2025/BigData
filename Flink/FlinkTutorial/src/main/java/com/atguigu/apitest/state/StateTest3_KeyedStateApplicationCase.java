package com.atguigu.apitest.state;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：
 * 假设做一个温度报警，如果一个传感器前后温差超过10度就报警。
 * 这里使用键控状态Keyed State + flatMap来实现
 * https://ashiamd.github.io/docsify-notes/#/study/BigData/Flink/%E5%B0%9A%E7%A1%85%E8%B0%B7Flink%E5%85%A5%E9%97%A8%E5%88%B0%E5%AE%9E%E6%88%98-%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0?id=_737-%e7%aa%97%e5%8f%a3%e8%b5%b7%e5%a7%8b%e7%82%b9%e5%92%8c%e5%81%8f%e7%a7%bb%e9%87%8f
 * @date ：2021/3/10 上午11:20
 */


public class StateTest3_KeyedStateApplicationCase {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        SingleOutputStreamOperator<SensorReading> map = inputStream.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(split[0],Long.valueOf(split[1]),Double.valueOf(split[2]));
            }
        });

        SingleOutputStreamOperator<Tuple3<String, Double, Double>> name = map.keyBy("name").flatMap(new Temp10FlatMapper(Double.valueOf(10)));
        name.print("温度差值");
        env.execute();
    }
    public static class Temp10FlatMapper extends RichFlatMapFunction<SensorReading, Tuple3<String,Double,Double>> {
        private final Double threshold;
        private ValueState<Double> lastTem;
        public Temp10FlatMapper(Double t){
            threshold = t;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
//            super.open(parameters);
            lastTem = getRuntimeContext().getState(new ValueStateDescriptor<Double>("tem-double",Double.class));
        }

        @Override
        public void close() throws Exception {
            lastTem.clear();
            super.close();
        }

        @Override
        public void flatMap(SensorReading value, Collector<Tuple3<String, Double, Double>> out) throws Exception {
            Double lastTemp = lastTem.value();
            Double nowTemp = value.getTem();

            if(lastTemp != null){
                if(Math.abs(nowTemp-lastTemp)>=threshold){
                    out.collect(
                            new Tuple3<>(value.getName(), lastTemp, nowTemp)
                    );
                }
            }
            lastTem.update(nowTemp);
        }
    }
}
