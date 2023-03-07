package com.yzf.stream;

import com.yzf.source.RegisterProcessTimeFlinkTable;
import com.yzf.source.RegisterHiveCatelog;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 *按照处理时间的语义，将结果写入hive
 * 默认使用的是'sink.partition-commit.trigger'='process-time',
 * 'partition-time'暂时可以满足需求
 */
public class SinkToHiveWithProcessTime {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hiveConfDir = parameterTool.get("hive_conf_dir","/data/flink/emr_hive_conf/conf");
        // hive提交的时候按照checkpoint的时间进行提交，如果checkpoint太频繁，容易产生小文件过多的问题
        env.enableCheckpointing(1000*60);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        RegisterHiveCatelog.register(tableEnv,hiveConfDir);

        RegisterProcessTimeFlinkTable.registerSource(env,tableEnv);

        Table re = tableEnv.sqlQuery("select user_id,order_amount,dt,hr from source_table");
//        re.printSchema();
        tableEnv.toAppendStream(re, Row.class).print("table: ");

        re.executeInsert("flink_database.hive_table_process");
        // env不用提交，tableEnv已经提交了，
        //        env.execute();

    }
}
