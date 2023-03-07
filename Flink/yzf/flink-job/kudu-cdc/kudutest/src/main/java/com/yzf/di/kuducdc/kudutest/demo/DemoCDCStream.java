package com.yzf.di.kuducdc.kudutest.demo;

import com.yzf.di.kuducdc.kudutest.source.DemoAutoGenerateSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.RowOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

/**
 * 测试用，在kudu中建表，随机产生一些数据写入到kudu中。
 */
public class DemoCDCStream {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // --bootstrapSource 172.24.215.5:9093 --bootstrapTarget 172.24.215.5:9093
        String KUDU_MASTER = "172.24.10.2:7051";
        DataStreamSource<Tuple4<String, String, String, String>> source = new DemoAutoGenerateSource().source(env);
        KuduWriterConfig writerConfig = KuduWriterConfig.Builder.setMasters(KUDU_MASTER).build();
        String[] a = new String[]{"id", "cell_location", "cell_is_change","cell_value"};
        SingleOutputStreamOperator<Row> map = source.map(new MapFunction<Tuple4<String, String, String, String>, Row>() {
            @Override
            public Row map(Tuple4<String, String, String, String> value) throws Exception {
                Row row = new Row(4);
                row.setField(0, value.f0);
                row.setField(1, value.f1);
                row.setField(2, value.f2);
                row.setField(3, value.f3);
                return row;
            }
        }).setParallelism(1);

        map.print();
        KuduSink<Row> rowKuduSink = new KuduSink<Row>(
                writerConfig,
                KuduTableInfo.forTable("impala::test.octopus_qushu_cell_20210707"),
                new RowOperationMapper(
                        a,
                        AbstractSingleOperationMapper.KuduOperation.UPSERT)
        );
        map.addSink(rowKuduSink).setParallelism(2);
        env.execute("压力测试kudu");
    }
}
