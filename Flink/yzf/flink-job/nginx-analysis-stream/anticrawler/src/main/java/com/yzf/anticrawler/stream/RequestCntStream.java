package com.yzf.anticrawler.stream;

import com.google.common.io.Resources;
import com.yzf.anticrawler.sink.CSMHttpAlarmSink;
import com.yzf.anticrawler.bean.RequestCnt;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.descriptors.Kafka;
import org.apache.flink.table.descriptors.Rowtime;
import org.apache.flink.table.descriptors.Schema;
import org.apache.flink.table.sources.tsextractors.ExistingField;
import org.apache.flink.types.Row;
import org.apache.flink.table.descriptors.Json;
import org.apache.flink.api.common.typeinfo.Types;

import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Properties;

public class RequestCntStream {
    public static void main(String[] args) throws Exception {
        // 1、创建环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        // 2、连接kafka，读取数据
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // 测试环境：--csmHttpAlarmSink http://172.24.155.3:9050/open/api/pushCloseInfo
        String csmHttpAlarmSink = parameterTool.get("csmHttpAlarmSink","http://172.16.30.151:8088/open/api/pushCloseInfo");

        String ddl_sql = Resources.toString(Resources.getResource("ddl.sql"), StandardCharsets.UTF_8);
        tableEnv.sqlUpdate(ddl_sql);
        String select_sql = Resources.toString(Resources.getResource("select.sql"), StandardCharsets.UTF_8);

        // 3、读取sql，填充阈值
//        Table table = tableEnv.from("nginx_log_anticrawler_formatter");
        Table table = tableEnv.sqlQuery(select_sql);
        // 4、写入到csm结果流中
        DataStream<Row> rowDataStream = tableEnv.toAppendStream(table, Row.class);
        rowDataStream.print();
        rowDataStream.addSink(new CSMHttpAlarmSink(csmHttpAlarmSink));
        env.execute("nginx实时数据分析-2、重点接口预警");
    }
}

