package com.atguigu.apitest.source;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Csv;
import org.apache.flink.table.descriptors.FileSystem;
import org.apache.flink.table.descriptors.Json;
import org.apache.flink.table.descriptors.Schema;

import java.util.Properties;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从Kafka中读取数据，写到本地文件中
 * @date ：2021/3/16 下午1:41
 */


public class SourcePractice3_Kafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        Properties p = new Properties();
        p.put("bootstrap.servers","172.24.10.5:9092");
        p.put("group.id","source_practice3_kafka");
        p.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        p.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        p.put("auto.offset.reset","earliest");
        DataStreamSource<ObjectNode> source = env.addSource(new FlinkKafkaConsumer<>("nginx_log_anticrawler",
                new JSONKeyValueDeserializationSchema(false),
                p));

        SingleOutputStreamOperator<Tuple9<String, String, String, String, String, String, String, Double, Double>> map = source.map(new MapFunction<ObjectNode, Tuple9<String, String, String, String, String, String, String, Double, Double>>() {
            @Override
            public Tuple9<String, String, String, String, String, String, String, Double, Double> map(ObjectNode jsonNodes) throws Exception {
                // set,request,cookie_user_name,cookie_gs_id,ua_os_name,status,x_forwarded_for,request_length,request_length
                return new Tuple9<String, String, String, String, String, String, String, Double, Double>(
                        jsonNodes.get("value").get("fields-set").asText(),
                        jsonNodes.get("value").get("time_local").asText(),
                        jsonNodes.get("value").get("cookie_user_name").asText(),
                        jsonNodes.get("value").get("ua_os_name").asText(),
                        jsonNodes.get("value").get("x_forwarded_for").asText(),
                        jsonNodes.get("value").get("request").asText(),
                        jsonNodes.get("value").get("status").asText(),
                        Double.valueOf(jsonNodes.get("value").get("request_time").asText()),
                        Double.valueOf(jsonNodes.get("value").get("request_length").asText())
                );
            }
        });
        map.print();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromDataStream(map);
        tableEnv.createTemporaryView("nginx_user_log",table);
        Table table1 = tableEnv.sqlQuery("select f0 as fields_set,f1 as time_local,f2 as cookie_user_name,f3 as ua_os_name," +
                "f4 as x_forwarded_for,f5 as request,f6 as status,f7 as request_time,f8 as request_length from nginx_user_log");
        String output = "src/main/resources/output.csv";
        tableEnv.connect(new FileSystem().path(output))
                .withFormat(new Csv())
                .withSchema(new Schema()
                        .field("fields_set", DataTypes.STRING())
                        .field("time_local", DataTypes.STRING())
                        .field("cookie_user_name", DataTypes.STRING())
                        .field("ua_os_name", DataTypes.STRING())
                        .field("x_forwarded_for", DataTypes.STRING())
                        .field("request", DataTypes.STRING())
                        .field("status", DataTypes.STRING())
                        .field("request_time", DataTypes.DOUBLE())
                        .field("request_length", DataTypes.DOUBLE())
                ).createTemporaryTable("outputTable");
        table1.executeInsert("outputTable");
        env.execute();

    }
}
