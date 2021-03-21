package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import sun.management.Sensor;
import org.apache.flink.types.Row;
public class TableTest5_TumbleWindowSql {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        String file = "src/main/resources/sensor.txt";
        SingleOutputStreamOperator<SensorReading> map = env.readTextFile(file).map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading(
                        "sensor_" + split[0],
                        Long.valueOf(split[1]),
                        Double.valueOf(split[2])
                );
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(SensorReading element) {
                return element.getTimeStamp()*1000L;
            }
        });
//        map.print();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromDataStream(map, "name,timeStamp as ts,tem,rt.rowtime");
        table.printSchema();
        tableEnv.createTemporaryView("inputTable",table);
        String sql = "select name,count(1) as cnt,avg(tem),tumble_end(rt, interval '10' second) from inputTable group by name,tumble(rt, interval '10' second)";
        tableEnv.toRetractStream(tableEnv.sqlQuery(sql),Row.class).print();

        env.execute();
    }
}
