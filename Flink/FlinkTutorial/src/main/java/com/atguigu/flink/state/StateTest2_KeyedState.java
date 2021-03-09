package com.atguigu.flink.state;

import com.atguigu.flink.beans.SensorReading;
import org.apache.flink.api.common.functions.IterationRuntimeContext;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.state.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import scala.Int;
import sun.management.Sensor;

import java.util.List;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/9 下午4:00
 */


public class StateTest2_KeyedState {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 并行度1
        env.setParallelism(1);
        // 从本地socket读取数量
        DataStreamSource<String> socket = env.socketTextStream("localhost", 7777);
        // 转换为SensorReading类型
        SingleOutputStreamOperator<SensorReading> map = socket.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String s) throws Exception {
                String[] split = s.split(",");
                return new SensorReading(split[0],
                        Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        });
        // 使用自定义的map方法，
        SingleOutputStreamOperator<Integer> name = map.keyBy("name").map(new MyMapper());
        name.print("");
        env.execute();
    }

    private static class MyMapper extends RichMapFunction<SensorReading,Integer> {

        private ValueState<Integer> valueState;
        private MapState<String,Double> myMapState;
        private ListState<String> myListState;

        @Override
        public void open(Configuration parameters) throws Exception {
            valueState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("my-int",Integer.class));
            myListState = getRuntimeContext().getListState(new ListStateDescriptor<String>("my-list", String.class));
            myMapState = getRuntimeContext().getMapState(new MapStateDescriptor<String, Double>("my-map",String.class,Double.class));
        }

        @Override
        public void close() throws Exception {
            super.close();
        }
        // 使用
        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            Integer count = valueState.value();
            if(count == null)count = 0;
            ++count;
            valueState.update(count);
            //            System.out.println("当前sensor的个数"+valueState.value().toString());
            return count;

        }
    }
}
