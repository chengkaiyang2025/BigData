package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.bean.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Calendar;
import java.util.Random;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/14 下午4:06
 */


public class TableTest1_Practice {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
//        String file = "src/main/resources/sensor.txt";
        // 1.读取数据
        // 2.转为pojo数据类
//        SingleOutputStreamOperator<String> source = env.readTextFile(file);

        // 2（自定义数据源）
        DataStreamSource<SensorReading> map = env.addSource(new SourceFunction<SensorReading>() {
            boolean isRunning = true;
            Random r = new Random();
            @Override
            public void run(SourceContext<SensorReading> ctx) throws Exception {
                while (isRunning){
                    ctx.collect(new SensorReading(
                            "sensor_"+r.nextInt(10),
                            Calendar.getInstance().getTime().getTime(),
                            Double.valueOf(35+r.nextGaussian()*10)
                    ));
                    Thread.sleep(2000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

//        SingleOutputStreamOperator<SensorReading> map = source.map(new MapFunction<String, SensorReading>() {
//            @Override
//            public SensorReading map(String value) throws Exception {
//                String[] split = value.split(",");
//                return new SensorReading(split[0], Long.valueOf(split[1]), Double.valueOf(split[2]));
//            }
//        });
        // 3.创建表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 4.基于流创建一张表
        Table dataTable = tableEnv.fromDataStream(map);
        // 5.调用table Api进行转换操作。
        Table resultTable = dataTable.select("name, tem")
                .where("name = 'sensor_1'");

        tableEnv.createTemporaryView("sensor", dataTable);
        Table resultSqlTable = tableEnv.sqlQuery("select name,tem from sensor");
//                .where("tem > 34");
        // 6.执行SQL
        tableEnv.toAppendStream(resultTable, Row.class).print("Table API");
        tableEnv.toAppendStream(resultSqlTable, Row.class).print("SQL API");
//        map.print("元数据");
        env.execute();

    }
}
