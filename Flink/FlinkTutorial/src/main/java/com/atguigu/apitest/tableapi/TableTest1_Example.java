package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.runtime.executiongraph.Execution;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从文件系统中读取数据，先转为dataStream，再将dataStream转为Table。
 * @date ：2021/3/14 下午2:23
 */


public class TableTest1_Example {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file = "src/main/resources/sensor.txt";
        // 1.读取数据
        // 2.转为pojo数据类
        SingleOutputStreamOperator<SensorReading> map = env.readTextFile(file).map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
            }
        });
        // 3.创建表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 4.基于流创建一张表
        Table dataTable = tableEnv.fromDataStream(map);
        // 5.调用table Api进行转换操作。
        Table resultTable = dataTable.select("name, tem")
                .where("name = 'sensor_1'");

        tableEnv.createTemporaryView("sensor", dataTable);
        Table resultSqlTable = tableEnv.sqlQuery("select name, tem from sensor where name = 'sensor_1'");
//                .where("tem > 34");
        // 6.执行SQL
        tableEnv.toAppendStream(resultTable, Row.class).print("Table API");
        tableEnv.toAppendStream(resultSqlTable, Row.class).print("SQL API");
//        map.print("元数据");
        env.execute();

    }
}
