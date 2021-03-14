package com.atguigu.apitest.state;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Collections;
import java.util.List;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/9 下午3:36
 */


public class StateTest1_OperatorState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStream<SensorReading> source = env.socketTextStream("localhost", 7777)
                .map(new MapFunction<String, SensorReading>() {
                    @Override
                    public SensorReading map(String s) throws Exception {
                        String[] split = s.split(",");
                        return new SensorReading(split[0],Long.valueOf(split[1]),Double.valueOf(split[2]));
                    }
                });
        SingleOutputStreamOperator<Integer> map = source.map(new MyCountMapper());

        map.print();
        env.execute();

    }
    public static class MyCountMapper implements MapFunction<SensorReading, Integer>, ListCheckpointed<Integer>{

        // 定义一个本地变量，作为算子状态
        private Integer count = 0;
        @Override
        public Integer map(SensorReading sensorReading) throws Exception {
            count++;
            return count;
        }

        @Override
        public List<Integer> snapshotState(long checkpointId, long timestamp) throws Exception {
            return Collections.singletonList(count);
        }

        @Override
        public void restoreState(List<Integer> state) throws Exception {
            for (Integer num:state
                 ) {
                count += num;
            }
        }
    }
}
