package com.yzf.apianalysis.stream;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import com.google.common.io.Resources;


/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/23 16:00
 * @description：
 */
public class TableTest2_CommonApi {
    public static void main(String[] args) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度为1
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        String sourceDDL = " CREATE TABLE inputTable (\n" +
                "  id STRING,\n" +
                "  use_count BIGINT\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'nginx_log',\n" +
                " 'properties.bootstrap.servers' = '172.28.177.120:9092',\n" +
                " 'properties.group.id' = 'testGroup',\n" +
                " 'format' = 'json',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
                " 'json.ignore-parse-errors' = 'false'\n" +
                ") ";
        String sinkDDL = " CREATE TABLE outputTable (\n" +
                "  id1 STRING,\n" +
                "  use_count1 BIGINT,\n" +
                "  fetch_time1 BIGINT\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'nginx_result',\n" +
                " 'properties.bootstrap.servers' = '172.28.177.120:9092',\n" +
                " 'format' = 'json',\n" +
                " 'json.ignore-parse-errors' = 'false'\n" +
                ") ";

        String sinkSql = " insert into outputTable select id,use_count,0 from inputTable where id = 'api_669'";

        tableEnv.sqlUpdate(sourceDDL);
        tableEnv.sqlUpdate(sinkDDL);
        tableEnv.sqlUpdate(sinkSql);


        tableEnv.execute("analysis");
    }
}
