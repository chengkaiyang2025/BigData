package com.atguigu.apitest.tableapi;


import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
public class TableTest5_EventTimeDefinedInSql {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env,environmentSettings);
        String sql = "create table dataTable (\n" +
                "    id varchar(20) not null,\n" +
                "    ts bigint,\n" +
                "    tem double,\n" +
                "    rt as TO_TIMESTAMP(FROM_UNIXTIME(ts)),\n" +
                "    watermark for rt as rt - interval '1' second\n" +
                ") with (\n" +
                "    'connector.type' = 'filesystem',\n" +
                "    'connector.path' = 'src/main/resources/sensor.txt',\n" +
                "    'format.type' = 'csv'\n" +
                ")";
        tableEnv.sqlUpdate(sql);
        Table table = tableEnv.sqlQuery("select * from dataTable");
        tableEnv.toAppendStream(table,Row.class).print();
        env.execute();

    }
}
