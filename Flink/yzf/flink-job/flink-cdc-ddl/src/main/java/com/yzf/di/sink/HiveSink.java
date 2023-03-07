package com.yzf.di.sink;

import com.yzf.di.bean.ChangesAlert;
import com.yzf.di.source.RegisterHiveCatelog;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.io.IOException;

import static org.apache.flink.table.api.Expressions.$;

public class HiveSink {
    public void sink(StreamExecutionEnvironment env, DataStream<ChangesAlert> alertInfo, String hiveConfDir) throws IOException {

        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        // 1、注册hive
        RegisterHiveCatelog.register(tableEnv, hiveConfDir);
        // 2、从schema.sql中获得流表的字段，将流转为表
        Table table = tableEnv.fromDataStream(alertInfo,
                $("type"),
                $("address"),
                $("database_name"),
                $("table_name"),
                $("ddl"),
                $("op_time"));
        tableEnv.createTemporaryView("binlog", table);
        Table result = tableEnv.sqlQuery(
                "select \n" +
                    "type," +
                    "address," +
                    "database_name," +
                    "table_name," +
                    "ddl," +
                    "op_time," +
                    "substr(replace(op_time,'-',''),1,8) as dt \n" +
                "from binlog \n" +
                "where type = 'ALTER'");


//        tableEnv.toAppendStream(result, Row.class).print("binlog: ");

        result.executeInsert("log_ods.ods_jf_table_schema_changes");
    }
}
