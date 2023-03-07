package com.yzf.di.kuducdc.kudutest.pressure;

import com.yzf.di.kuducdc.kudutest.source.OctopusKafkaSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.RowOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

/** 测试宽表kudu的写入效率
 * 1、从kafka消费数据。
 * 2、在map算子中，sleep一段时间，强制静态控流，避免占用下一步sink算子的kudu资源;同时将算子并行度设置为1，让算子之间并行度不一致便于产生背压。
 * 3、在flatmap中将数据拉平，经2021年7月线上观测评价一条json会被拆分100份，因此上一步算子的静态控流很重要。
 * 4、sink算子写入到kudu
 */
public class OctopusWideCDCStream {
    /** 校验JSON中的格式，如果没有key则补充Key
     * https://blog.csdn.net/liuxiao723846/article/details/108578544
     * @param data
     * @return
     */
    public static ObjectNode JsonSchemaValidator(ObjectNode data){
        if (data.get("id") == null)  data.put("id","NULL");
        if (data.get("cell_location") == null)  data.put("cell_location","NULL");
        if (data.get("cell_is_change") == null)  data.put("cell_is_change","NULL");
        if (data.get("cell_value") == null)  data.put("cell_value","NULL");
        if (data.get("area_id") == null)  data.put("area_id","NULL");
        if (data.get("area_name") == null)  data.put("area_name","NULL");
        if (data.get("box_id") == null)  data.put("box_id","NULL");
        if (data.get("create_time") == null)  data.put("create_time","NULL");
        if (data.get("dz_qy_id") == null)  data.put("dz_qy_id","NULL");
        if (data.get("dz_qy_name") == null)  data.put("dz_qy_name","NULL");
        if (data.get("fetch_data_time") == null)  data.put("fetch_data_time",1L);
        if (data.get("kjnd") == null)  data.put("kjnd","NULL");
        if (data.get("kjqj") == null)  data.put("kjqj","NULL");
        if (data.get("nsqxdm") == null)  data.put("nsqxdm","NULL");
        if (data.get("parent_box_id") == null)  data.put("parent_box_id","NULL");
        if (data.get("qy_id") == null)  data.put("qy_id","NULL");
        if (data.get("qy_name") == null)  data.put("qy_name","NULL");
        if (data.get("sbsz_id") == null)  data.put("sbsz_id","NULL");
        if (data.get("sheet_name") == null)  data.put("sheet_name","NULL");
        if (data.get("system_id") == null)  data.put("system_id","NULL");
        return data;
    }
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        ParameterTool tool = ParameterTool.fromArgs(args);
        //
        String KUDU_MASTER = tool.get("kudu_master","172.24.10.2:7051");
        String KUDU_TABLE = tool.get("kudu_table","impala::octopus_ods.octopus_qushu_table_wide_202106");

        String BOOTSTRAP = tool.get("bootstrap","172.24.215.5:9093");
        // --max_process_amount 100000
        Integer MAX_PROCESS_AMOUNT = Integer.valueOf(tool.get("max_process_amount", "1000"));
//        DataStream<ObjectNode> source = new OctopusAutoGenerateSource().source(env);
        DataStream<ObjectNode> source = new OctopusKafkaSource().source(env, BOOTSTRAP);

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

        SingleOutputStreamOperator<Row> flatMap = source
                .map(new MapFunction<ObjectNode, ObjectNode>() {
                    // 强制sleep做限流
                    @Override
                    public ObjectNode map(ObjectNode jsonNodes) throws Exception {
//                        Thread.sleep(1000/MAX_PROCESS_AMOUNT);
                        return jsonNodes;
                    }
                }).setParallelism(1)
                .flatMap(new FlatMapFunction<ObjectNode, Row>() {
            @Override
            public void flatMap(ObjectNode value, Collector<Row> out) throws Exception {
                ObjectNode data = (ObjectNode) value.get("value");
                data = JsonSchemaValidator(data);

                for (JsonNode cell : data.findValue("cells")) {
                    Row row = new Row(columns.length);
                    row.setField(0, data.get("id").asText());
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
                    row.setField(4, data.get("area_id").asText());
                    row.setField(5, data.get("area_name").asText());
                    row.setField(6, data.get("box_id").asText());
                    row.setField(7, data.get("create_time").asText());
                    row.setField(8, data.get("dz_qy_id").asText());
                    row.setField(9, data.get("dz_qy_name").asText());
                    row.setField(10, data.get("fetch_data_time").asLong());
                    row.setField(11, data.get("kjnd").asText());
                    row.setField(12, data.get("kjqj").asText());
                    row.setField(13, data.get("nsqxdm").asText());
                    row.setField(14, data.get("parent_box_id").asText());
                    row.setField(15, data.get("qy_id").asText());
                    row.setField(16, data.get("qy_name").asText());
                    row.setField(17, data.get("sbsz_id").asText());
                    row.setField(18, data.get("sheet_name").asText());
                    row.setField(19, data.get("system_id").asText());

                    out.collect(row);
                }
            }}).setParallelism(1);


        KuduSink<Row> rowKuduSink = new KuduSink<Row>(
                writerConfig,
                KuduTableInfo.forTable(KUDU_TABLE),
                new RowOperationMapper(
                        columns,
                        AbstractSingleOperationMapper.KuduOperation.UPSERT)
        );

        flatMap.addSink(rowKuduSink).setParallelism(3);
        source.print();
        env.execute();
    }
}
