package com.yzf.demo;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;
import org.apache.flink.types.Row;

import java.util.Random;

/**
 * hive官方文档没有写如果通过java代码写入hive
 * https://zhuanlan.zhihu.com/p/157899980
 */
public class WriteToHive {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        env.enableCheckpointing(10000);
        DataStreamSource<Tuple2<String, Double>> source = env.addSource(new SourceFunction<Tuple2<String, Double>>() {
            final Random r = new Random();
            boolean isRunning = true;

            @Override
            public void run(SourceContext<Tuple2<String, Double>> sourceContext) throws Exception {
                while (isRunning){
                    sourceContext.collect(new Tuple2<String, Double>("key" + r.nextInt(3), r.nextDouble()))
                    ;
                    Thread.sleep(1000);
                }
            }


            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        Table table = tableEnv.fromDataStream(source);
        tableEnv.createTemporaryView("kv",table);
        table.printSchema();
        Table re = tableEnv.sqlQuery("select f0 as user_id,f1 as order_amount,'20210301' as dt from kv where f0 = 'key1' ");
//        source.print();
        re.printSchema();
        tableEnv.toAppendStream(re,Row.class).print("table: ");

        String name = "myhive";
        String defaultDatabase = "default";
        String hiveConfDir = "/data/yzf/IdeaWorkSpace/flink-job/operation/operation/flink-hive-connector-test/src/main/resources/conf"; // a local path
        String version = "2.1.1";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir, version);
        tableEnv.registerCatalog("myhive", hive);
        tableEnv.useCatalog("myhive");
        tableEnv.useDatabase("dwd");
        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);
        re.executeInsert("flink_database.hive_table");
//        tableEnv.executeSql("CREATE TABLE hive_table (\n" +
//                "  user_id STRING,\n" +
//                "  order_amount DOUBLE\n" +
//                ") PARTITIONED BY (dt STRING, hr STRING) STORED AS parquet TBLPROPERTIES (\n" +
////                "  'partition.time-extractor.timestamp-pattern'='$dt $hr:00:00',\n" +
////                "  'sink.partition-commit.trigger'='partition-time',\n" +
////                "  'sink.partition-commit.delay'='1 h',\n" +
////                "  'sink.partition-commit.watermark-time-zone'='Asia/Shanghai', -- Assume user configured time zone is 'Asia/Shanghai'\n" +
//                "  'sink.partition-commit.policy.kind'='metastore,success-file'\n" +
//                ");");
//        Table table = tableEnv.sqlQuery("insert into dwd.myhive select ");
//        table.printSchema();

//        tableEnv.execute("");
        env.execute();
    }
}
