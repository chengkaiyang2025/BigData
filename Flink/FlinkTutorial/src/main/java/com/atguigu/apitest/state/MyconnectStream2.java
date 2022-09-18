package com.atguigu.apitest.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.Optional;

public class MyconnectStream2 {

    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> streamSource = environment.fromCollection(Arrays.asList("1", "2", "3"));
        DataStreamSource<Integer> source = environment.fromCollection(Arrays.asList(1, 2, 3,4));

        streamSource.connect(source).keyBy(new KeySelector<String, Object>() {
            /**
             * @param s 输入的 "1", "2", "3"
             * @return 统一输出 0 放到0分区
             * @throws Exception
             */
            @Override
            public Object getKey(String s) throws Exception {
                return 0;
            }
        }, new KeySelector<Integer, Object>() {
            /**
             * @param integer 输入的1, 2, 3,4
             * @return 统一输出 0 放到0分区
             * @throws Exception
             */
            @Override
            public Object getKey(Integer integer) throws Exception {
                return 0;
            }
        }).process(new CoProcessFunction<String, Integer, Object>() {
            /**
             * 初始化状态后端描述符，data，数据类型为value，还可以为MapState<K,V>,ListState<IN>
             * 用于存储状态数据，根据状态后端的选择决定存储在TaskManager内存、本地文件系统、或rocksDB。
             */
            private ValueState<Integer> data;

            /** 定义全局唯一的标识符
             * @param parameters
             * @throws Exception
             */
            @Override
            public void open(Configuration parameters) throws Exception {
                data = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("my-int",Integer.class));
            }

            @Override
            public void processElement1(String s, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                if(data.value() == null){
                    collector.collect("####"+s+"***");
                }else collector.collect(data.value().toString()+"####"+s+"***");
            }

            @Override
            public void processElement2(Integer integer, CoProcessFunction<String, Integer, Object>.Context context, Collector<Object> collector) throws Exception {
                data.update(integer*3);
                collector.collect(integer*3);

            }
        }).print("D");
        environment.execute();
    }
}
