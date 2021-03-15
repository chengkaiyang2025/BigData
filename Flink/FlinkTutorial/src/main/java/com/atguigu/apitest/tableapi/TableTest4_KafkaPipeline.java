package com.atguigu.apitest.tableapi;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.types.DataType;

import javax.xml.crypto.Data;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/15 下午6:13
 */


public class TableTest4_KafkaPipeline {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        env.setParallelism(1);
        String file = "src/main/resources/sensor.txt";
        tableEnv.connect(new FileSystem().path(file))
                .withFormat(new Csv())
                .withSchema(new Schema()
                .field("id", DataTypes.STRING())
                .field("ts", DataTypes.BIGINT())
                .field("temp", DataTypes.DOUBLE())
                ).createTemporaryTable("inputTable");

        Table resultTable = tableEnv.sqlQuery("select id,temp from inputTable");
        String outputFile = "src/main/resources/out2.txt";
        tableEnv.connect(new FileSystem().path(outputFile))
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("id",DataTypes.STRING())
                        .field("temp",DataTypes.DOUBLE()))
                .createTemporaryTable("outputTable");
        resultTable.insertInto("outputTable");

        env.execute();
    }
}
