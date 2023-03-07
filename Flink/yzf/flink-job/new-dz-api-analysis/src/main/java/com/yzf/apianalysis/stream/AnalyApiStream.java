package com.yzf.apianalysis.stream;

import com.yzf.apianalysis.sink.DingAlertingSink;
import com.yzf.apianalysis.udf.TimeFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import com.google.common.io.Resources;
import org.apache.flink.types.Row;

import java.nio.charset.StandardCharsets;


/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/19 17:31
 * @description：
 */
public class AnalyApiStream {


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 注册自定义函数
        tableEnv.createTemporarySystemFunction("UDTimeFormat2UnixTime", TimeFunction.UDTimeFormat2UnixTime.class);

        String source_sql = Resources.toString(Resources.getResource("source.sql"), StandardCharsets.UTF_8);
        tableEnv.sqlUpdate(source_sql);

        String select_sql = Resources.toString(Resources.getResource("select.sql"), StandardCharsets.UTF_8);
        Table result = tableEnv.sqlQuery(select_sql);

//        tableEnv.toRetractStream(result, Row.class).print();
//        tableEnv.toAppendStream(result, Row.class).print();
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(result, Row.class);
        rowDataStream.print();

        // 发送到钉钉群进行告警
        rowDataStream.addSink(new DingAlertingSink());
        env.execute("new-accouning-nginx-api-analysis");

    }
}


