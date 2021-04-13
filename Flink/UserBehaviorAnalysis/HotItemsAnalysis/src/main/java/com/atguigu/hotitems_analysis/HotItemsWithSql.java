package com.atguigu.hotitems_analysis;

import com.atguigu.hotitems_analysis.bean.UserBehavior;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

public class HotItemsWithSql {

    public static void main(String[] args) throws Exception {
        // 1、启动
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        // 2、读取数据源
        String file = "/home/yzf/IdeaProjects/Bigdata/Flink/UserBehaviorAnalysis/HotItemsAnalysis/src/main/resources/UserBehavior.csv";
        DataStreamSource<String> source = env.readTextFile(file);
        // 3、转为pojo，时间戳与watermark
        SingleOutputStreamOperator<UserBehavior> map = source.map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String value) throws Exception {
                String[] split = value.split(",");

                return new UserBehavior(
                        new Long(split[0]),
                        new Long(split[1]),
                        new Integer(split[2]),
                        split[3],
                        new Long(split[4])
                );
            }
        });
        // 4、创建表执行环境，使用blink版本
//        EnvironmentSettings settings = EnvironmentSettings.newInstance()
//                .useBlinkPlanner()
//                .inStreamingMode()
//                .build();
//
//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, settings);
//        // 5、将流转为表
//        tableEnv.createTemporaryView("data_table",map,"itemId,behavior,timestamp.rowtime as ts");
//
//        Table resultSqltable = tableEnv.sqlQuery("select * from data_table where behavior = 'pv'");
//
//        // 6、纯sql实现
//        tableEnv.toRetractStream(resultSqltable,Row.class).print();
        map.print();
        env.execute();
    }
}
