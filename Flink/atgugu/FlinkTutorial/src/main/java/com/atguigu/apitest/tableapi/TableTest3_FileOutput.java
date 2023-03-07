package com.atguigu.apitest.tableapi;

import org.apache.commons.math3.fitting.leastsquares.EvaluationRmsChecker;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Schema;

import java.awt.event.TextEvent;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/14 下午5:12
 */


public class TableTest3_FileOutput {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String file = "src/main/resources/sensor.txt";
        tableEnv.connect(new FileSystem().path(file))
                .withFormat(new Csv())
                .withSchema(new Schema()
                .field("id", DataTypes.STRING())
                .field("timestamp",DataTypes.BIGINT())
                .field("temp",DataTypes.DOUBLE())
                ).createTemporaryTable("inputTable");

        Table resultTable = tableEnv.sqlQuery("select id,temp from inputTable where id = 'sensor_6'");

        String outputFile = "src/main/resources/out.txt";
        tableEnv.connect(new FileSystem().path(outputFile))
                .withFormat(new Csv())
                .withSchema(new Schema()
                .field("id",DataTypes.STRING())
                .field("tem",DataTypes.DOUBLE()))
                .createTemporaryTable("outputTable");
        resultTable.insertInto("outputTable");

        env.execute();










    }
}
