package com.yzf.di.stream;

import org.apache.flink.connectors.kudu.connector.KuduTableInfo;
import org.apache.flink.connectors.kudu.connector.writer.AbstractSingleOperationMapper;
import org.apache.flink.connectors.kudu.connector.writer.KuduWriterConfig;
import org.apache.flink.connectors.kudu.connector.writer.RowOperationMapper;
import org.apache.flink.connectors.kudu.streaming.KuduSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.Row;
import com.yzf.di.source.NginxKafkaSource;

import java.io.InputStream;
import java.util.Properties;


public class NginxCDCStream {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        Properties properties = new Properties();
        InputStream inputStream = NginxCDCStream.class.getClassLoader().getResourceAsStream("para.properties");
        properties.load(inputStream);
        String KUDU_MASTER = properties.getProperty("kudu_master");
        String KUDU_TABLE = properties.getProperty("kudu_table");


        DataStream<Row> kafkaSourceStream = new NginxKafkaSource().source(env);

        KuduWriterConfig writerConfig = KuduWriterConfig.Builder.setMasters(KUDU_MASTER).build();

        String[] columns = new String[]{
                "request",
                "n_offset",
                "base_url",
                "upstream_addr_nm",
                "request_length",
                "http_cookie",
                "fields_log_es_index",
                "fields_log_item",
                "fields_apptype",
                "fields_set",
                "http_referer",
                "remote_addrx",
                "remote_user",
                "scheme",
                "body_bytes_sent",
                "status",
                "host_name",
                "x_forwarded_for",
                "nginx",
                "proxy_add_x_forwarded_for",
                "beat_hostname",
                "beat_name",
                "beat_version",
                "source",
                "time_local",
                "http_user_agent",
                "upstream_addr",
                "request_time",
                "ssl_cipher"
        };


        KuduSink<Row> rowKuduSink = new KuduSink<Row>(writerConfig,
                KuduTableInfo.forTable(KUDU_TABLE),
                new RowOperationMapper(columns, AbstractSingleOperationMapper.KuduOperation.UPSERT));

        kafkaSourceStream.addSink(rowKuduSink).setParallelism(4);

        kafkaSourceStream.print();
        env.execute("new accouting nginx log from kafka to kudu");
    }
}
