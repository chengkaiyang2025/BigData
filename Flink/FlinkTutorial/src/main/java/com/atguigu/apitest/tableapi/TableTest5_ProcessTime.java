package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReading;
import com.atguigu.apitest.beans.SensorReadingSimple;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.expressions.Rand;
import org.apache.flink.types.Row;

import java.math.BigInteger;
import java.util.Random;

public class TableTest5_ProcessTime {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<SensorReading> source = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<SensorReading> ctx) throws Exception {
                while (isRunning) {
                    ctx.collect(new SensorReading(
                            "sensor_" + r.nextInt(10),
                            Double.valueOf(r.nextGaussian() * 100 + 50)
                    ));
                    Thread.sleep(1000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//        Table from = tableEnv.fromDataStream( source,);
        tableEnv.createTemporaryView("inputTable",source,"name,timeStamp as ts,tem,pc.proctime");
        Table table = tableEnv.sqlQuery("select * from inputTable");
        table.printSchema(); // pc: TIMESTAMP(3) *PROCTIME* 精度为3的timestamp

                tableEnv.toAppendStream(table, Row.class).print("增加事件时间");
        env.execute();
    }

}
