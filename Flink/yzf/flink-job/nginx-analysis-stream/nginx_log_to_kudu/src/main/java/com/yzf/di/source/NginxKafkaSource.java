package com.yzf.di.source;

import com.google.common.io.Resources;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NginxKafkaSource {
    public DataStream<Row> source(StreamExecutionEnvironment env) throws IOException {

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String source_sql = Resources.toString(Resources.getResource("source.sql"), StandardCharsets.UTF_8);
        tableEnv.executeSql(source_sql);

        String select_sql = Resources.toString(Resources.getResource("select.sql"), StandardCharsets.UTF_8);
        Table result = tableEnv.sqlQuery(select_sql);

        DataStream<Row> source = tableEnv.toAppendStream(result, Row.class);

        return source;
    }
}
