package com.atguigu.apitest.tableapi;

import com.atguigu.apitest.beans.SensorReadingSimple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Json;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Schema;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：自定义数据源，然后写入到Table中，传入到kafka
 * @date ：2021/3/16 上午11:19
 */


public class TableTest4_KafkaPipeline2 {
    public static void main(String[] args) throws Exception {
        // 随机生成数据流 SensorReadingSimple
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String,Double>> source = env.addSource(new SourceFunction<Tuple2<String,Double>>() {
            boolean isRunning = true;
            Random r = new Random();

            @Override
            public void run(SourceContext<Tuple2<String,Double>> ctx) throws Exception {
                while (isRunning) {
                    ctx.collect(new Tuple2(
                            "sensor_" + r.nextInt(10),
                            (-50 + r.nextGaussian() * 100)
                    ));
                    Thread.sleep(200);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        // 将流转为table

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromDataStream(source);
        table.printSchema();
        tableEnv.createTemporaryView("sensor_reading_simple",table);

        // 筛选0度以下的低温流写入table
        Table sinkTable = tableEnv.sqlQuery("select f0 as name,f1 as temp from sensor_reading_simple where f1 < 0");

        // 将table链接到外部kafka
        tableEnv.connect(new Kafka()
        .version("universal")
        .topic("low_temp_sensor")
                        .property("zookeeper.connect", "172.24.10.5:2181")
                        .property("bootstrap.servers", "172.24.10.5:9092")
        ).withFormat(new Json())
                .withSchema(new Schema()
                .field("name", DataTypes.STRING())
                .field("temp",DataTypes.DOUBLE())
                ).createTemporaryTable("low_temp_sensor");

        sinkTable.insertInto("low_temp_sensor");
        source.print("源数据");
        env.execute();
    }
}
