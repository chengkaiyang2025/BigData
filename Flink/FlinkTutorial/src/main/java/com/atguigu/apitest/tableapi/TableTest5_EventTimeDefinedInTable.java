package com.atguigu.apitest.tableapi;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Rowtime;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.types.Row;
/**
 * 使用table api直接定义事件事件
 */
public class TableTest5_EventTimeDefinedInTable {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        EnvironmentSettings build = EnvironmentSettings.newInstance().inStreamingMode().useBlinkPlanner().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,build);

        String file = "src/main/resources/sensor.txt";
        // 可能为官网bug
        tableEnv.connect(new FileSystem().path(file))
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("name", DataTypes.STRING())
                        .field("ts", DataTypes.BIGINT())
                        .rowtime(new Rowtime().timestampsFromField("ts").watermarksPeriodicBounded(1000))
                        .field("tem", DataTypes.DOUBLE())
                ).createTemporaryTable("inputTable");

        // api可能有bug，查询不出来

        Table table = tableEnv.sqlQuery("select name,ts as rt,tem from inputTable");
        table.printSchema();
        tableEnv.toAppendStream(table,Row.class).print();
        env.execute();
    }
}
