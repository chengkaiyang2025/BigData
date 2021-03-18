package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
public class TableTest5_TumbleWindow {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        String file = "src/main/resources/sensor.txt";
        SingleOutputStreamOperator<SensorReading> source = env.readTextFile(file).map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return new SensorReading("sensor_" + split[0]
                        , Long.valueOf(split[1])
                        , Double.valueOf(split[2]));
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(SensorReading element) {
                return element.getTimeStamp()*1000L;
            }
        });
        Table table = tableEnv.fromDataStream(source,"name,timeStamp as ts,tem,rt.rowtime");
        table.printSchema();
        Table resultTable = table.window(Tumble.over("10.seconds").on("rt").as("tw"))
                .groupBy("name, tw")
                .select("name,name.count,tem.avg,tw.end");
        tableEnv.toRetractStream(resultTable,Row.class).print();
        env.execute();
    }
}
