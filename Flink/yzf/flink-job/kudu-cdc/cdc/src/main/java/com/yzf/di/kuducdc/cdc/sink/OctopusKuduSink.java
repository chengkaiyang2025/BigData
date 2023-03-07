package com.yzf.di.kuducdc.cdc.sink;

import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.RowOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;

public class OctopusKuduSink {

    public KuduSink<Row> sink(StreamExecutionEnvironment env, String KUDU_MASTER,
                              String KUDU_TABLE,String[] columns){
        KuduWriterConfig writerConfig = KuduWriterConfig.Builder.setMasters(KUDU_MASTER).build();

        KuduSink<Row> rowKuduSink = new KuduSink<Row>(
                writerConfig,
                KuduTableInfo.forTable(KUDU_TABLE),
                new RowOperationMapper(
                        columns,
                        AbstractSingleOperationMapper.KuduOperation.UPSERT)
        );
        return rowKuduSink;
    }
}
