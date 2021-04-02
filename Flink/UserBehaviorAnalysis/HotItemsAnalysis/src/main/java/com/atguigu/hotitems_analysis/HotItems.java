package com.atguigu.hotitems_analysis;

import com.atguigu.hotitems_analysis.bean.UserBehavior;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class HotItems {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        String file = "/home/yzf/IdeaProjects/Bigdata/Flink/UserBehaviorAnalysis/HotItemsAnalysis/src/main/resources/UserBehavior.csv";
        DataStreamSource<String> source = env.readTextFile(file);
        SingleOutputStreamOperator<UserBehavior> sourceUser = source.map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String value) throws Exception {
                String[] fields = value.split(",");
                return new UserBehavior(
                        new Long(fields[0]), new Long(fields[1]), new Integer(fields[2]), fields[3], new Long(fields[4])
                );
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<UserBehavior>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(UserBehavior element) {
                return element.getTimestamp();
            }
        });
        sourceUser.print();
        env.execute();
    }
}
