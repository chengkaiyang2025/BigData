package com.yzf.di;

import com.google.gson.Gson;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import com.yzf.di.bean.ChangesAlert;
import com.yzf.di.bean.SchemaChange;
import com.yzf.di.sink.DingAlertingSink;
import com.yzf.di.sink.DingTalkAlert;
import com.yzf.di.sink.HiveSink;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yzf.di.bean.DingTalkCommon.DB_ADDRESS;
import static com.yzf.di.bean.DingTalkCommon.PROPERTIES_PATH;
import static org.apache.flink.table.api.Expressions.$;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/19 15:57
 * @description：
 */
public class TableSchemaChangesAlertMain {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 10分钟提交一次checkpoint
        env.enableCheckpointing(1000 * 60 * 30).setParallelism(4);

        ParameterTool parameterTool = ParameterTool.fromPropertiesFile(PROPERTIES_PATH);
        String hostname = parameterTool.get("hostname");
        int port = parameterTool.getInt("port");
        String username = parameterTool.get("username");
        String password = parameterTool.get("password");
        String databaseList = parameterTool.get("databaseList");
        String connectAddress = parameterTool.get("connectAddress");
        String hiveConfDir = parameterTool.get("hive_conf_dir");

        // 通过FlinkCDC构建SourceFunction
        DebeziumSourceFunction<String> sourceFunction = MySqlSource.<String>builder()
                .hostname(hostname)
                .port(port)
                .username(username)
                .password(password)
                .databaseList(databaseList)
                .deserializer(new JsonDebeziumDeserializationSchema())
                .startupOptions(StartupOptions.latest())
                .build();
        DataStream<String> dataStream = env.addSource(sourceFunction);

//        DataStream<ChangesAlert> mapStream = dataStream.map(new MapFunction<String, ChangesAlert>() {
//            @Override
//            public ChangesAlert map(String value) throws Exception {
//                Gson gson = new Gson();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                SchemaChange schemaChange = gson.fromJson(value, SchemaChange.class);
//                ChangesAlert changesAlert = new ChangesAlert();
//
//                if (schemaChange.getTableChanges() != null) {
//                    if (schemaChange.getTableChanges().size() > 0) {
//                        changesAlert.setType(schemaChange.getTableChanges().get(0).getType());
//                        changesAlert.setAddress(connectAddress);
//                        changesAlert.setDatabase_name(schemaChange.getSource().getDb());
//                        changesAlert.setTable_name(schemaChange.getSource().getTable());
//                        changesAlert.setDdl(schemaChange.getDdl());
//                        changesAlert.setOp_time(simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(schemaChange.getSource().getTs_ms())))));
//                    }
//                } else {
//                    changesAlert.setType("undefined");
//                }
//                // 此处想将value传入预警，但是并不可行，跑不通
////                DingTalkAlert.sendAlert(changesAlert);
//                return changesAlert;
//            }
//        });

//        dataStream.print("dataStream：").setParallelism(1);

        // 发送钉钉预警
        dataStream.addSink(new DingAlertingSink());

        // 写入hive
//        new HiveSink().sink(env, mapStream, hiveConfDir);

        env.execute("Table Schema Changes Alerting");

    }

}
