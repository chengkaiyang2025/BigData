package com.yzf.di.stream;

import com.google.common.io.Resources;
import com.yzf.di.udf.ParseCookie2GsId;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import com.yzf.di.sink.DingAlertingSink;
import com.yzf.di.udf.TimeFunction;

import java.nio.charset.StandardCharsets;


/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/19 17:31
 * @description：
 */
public class AnalyApiStream {


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        // 10分钟提交一次checkpoint
        env.enableCheckpointing(1000 * 60 * 10);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 注册自定义函数
        tableEnv.createTemporarySystemFunction("UDTimeFormat2UnixTime", TimeFunction.UDTimeFormat2UnixTime.class);
//        tableEnv.createTemporarySystemFunction("ParseCookie2GsId", ParseCookie2GsId.class);

        String source_sql = Resources.toString(Resources.getResource("source.sql"), StandardCharsets.UTF_8);
        tableEnv.executeSql(source_sql);

        String select_sql = Resources.toString(Resources.getResource("select.sql"), StandardCharsets.UTF_8);
        Table result = tableEnv.sqlQuery(select_sql);

        DataStream<Row> rowDataStream = tableEnv.toAppendStream(result, Row.class);

//        rowDataStream.print();

        // 发送到钉钉群进行告警
        rowDataStream.addSink(new DingAlertingSink());

        env.execute("New Accounting ApiInfo Analysis");

    }
}


