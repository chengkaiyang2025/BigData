package com.yzf.di.kuducdc.kudutest.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.RowOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/6/22 15:21
 * @description：
 */
public class DemoKafka2Kudu {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        // 线上kafka
//        properties.setProperty("bootstrap.servers", "172.22.99.236:9092");
        // 线下本机kafka
//        properties.setProperty("bootstrap.servers", "172.31.188.54:9092");
        //线下集群kafka
        properties.setProperty("bootstrap.servers", "172.24.215.5:9093");

        properties.setProperty("group.id", "kudu-cdc");

//        FlinkKafkaConsumer<ObjectNode> consumer = new FlinkKafkaConsumer<>("oc2", new JSONKeyValueDeserializationSchema(true), properties);
        FlinkKafkaConsumer<ObjectNode> consumer = new FlinkKafkaConsumer<>("octopus_fetchData_topic", new JSONKeyValueDeserializationSchema(true), properties);
        consumer.setStartFromEarliest();
        DataStreamSource<ObjectNode> streamSource = env.addSource(consumer).setParallelism(10);

        String KUDU_MASTER = "172.24.10.2:7051";
        KuduWriterConfig writerConfig = KuduWriterConfig.Builder.setMasters(KUDU_MASTER).build();
        String[] columns = new String[]{
                "id",
                "cell_location",
                "cell_is_change",
                "cell_value",
                "area_id",
                "area_name",
                "box_id",
                "create_time",
                "dz_qy_id",
                "dz_qy_name",
                "fetch_data_time",
                "kjnd",
                "kjqj",
                "nsqxdm",
                "parent_box_id",
                "qy_id",
                "qy_name",
                "sbsz_id",
                "sheet_name",
                "system_id"
        };

        SingleOutputStreamOperator<Row> flatMap = streamSource.flatMap(new FlatMapFunction<ObjectNode, Row>() {
            @Override
            public void flatMap(ObjectNode value, Collector<Row> out) throws Exception {
                ObjectNode data = (ObjectNode) value.get("value");
                for (JsonNode cell : data.findValue("cells")) {
                    Row row = new Row(columns.length);
                    if (data.get("id") == null) {
                        row.setField(0, "");
                    } else {
                        row.setField(0, data.get("id").asText());
                    }
                    if (cell.get("location") == null) {
                        row.setField(1, "");
                    } else {
                        row.setField(1, cell.get("location").asText());
                    }
                    if (cell.get("isChange") == null) {
                        row.setField(2, "");
                    } else {
                        row.setField(2, cell.get("isChange").asText());
                    }
                    if (cell.get("value") == null) {
                        row.setField(3, "");
                    } else {
                        row.setField(3, cell.get("value").asText());
                    }
                    if (data.get("areaId") == null) {
                        row.setField(4, "");
                    } else {
                        row.setField(4, data.get("areaId").asText());
                    }
                    if (data.get("areaName") == null) {
                        row.setField(5, "");
                    } else {
                        row.setField(5, data.get("areaName").asText());
                    }
                    if (data.get("boxId") == null) {
                        row.setField(6, "");
                    } else {
                        row.setField(6, data.get("boxId").asText());
                    }
                    if (data.get("createTime") == null) {
                        row.setField(7, "");
                    } else {
                        row.setField(7, data.get("createTime").asText());
                    }
                    if (data.get("dzQyId") == null) {
                        row.setField(8, "");
                    } else {
                        row.setField(8, data.get("dzQyId").asText());
                    }
                    if (data.get("dzQyName") == null) {
                        row.setField(9, "");
                    } else {
                        row.setField(9, data.get("dzQyName").asText());
                    }
                    if (data.get("fetchDataTime") == null) {
                        row.setField(10, 0L);
                    } else {
                        row.setField(10, data.get("fetchDataTime").asLong());
                    }
                    if (data.get("kjnd") == null) {
                        row.setField(11, "");
                    } else {
                        row.setField(11, data.get("kjnd").asText());
                    }
                    if (data.get("kjqj") == null) {
                        row.setField(12, "");
                    } else {
                        row.setField(12, data.get("kjqj").asText());
                    }
                    if (data.get("nsqxdm") == null) {
                        row.setField(13, "");
                    } else {
                        row.setField(13, data.get("nsqxdm").asText());
                    }
                    if (data.get("parentBoxId") == null) {
                        row.setField(14, "");
                    } else {
                        row.setField(14, data.get("parentBoxId").asText());
                    }
                    if (data.get("qyId") == null) {
                        row.setField(15, "");
                    } else {
                        row.setField(15, data.get("qyId").asText());
                    }
                    if (data.get("qyName") == null) {
                        row.setField(16, "");
                    } else {
                        row.setField(16, data.get("qyName").asText());
                    }
                    if (data.get("sbszId") == null) {
                        row.setField(17, "");
                    } else {
                        row.setField(17, data.get("sbszId").asText());
                    }
                    if (data.get("sheetName") == null) {
                        row.setField(18, "");
                    } else {
                        row.setField(18, data.get("sheetName").asText());
                    }
                    if (data.get("systemId") == null) {
                        row.setField(19, "");
                    } else {
                        row.setField(19, data.get("systemId").asText());
                    }

                    out.collect(row);
                }
            }
        });
        KuduSink<Row> rowKuduSink = new KuduSink<Row>(
                writerConfig,
                KuduTableInfo.forTable("impala::test.octopus_qushu_2021q2"),
                new RowOperationMapper(
                        columns,
                        AbstractSingleOperationMapper.KuduOperation.UPSERT)
        );
        flatMap.addSink(rowKuduSink);
        streamSource.print();
        env.execute("data from kafka to kudu");

    }
}
