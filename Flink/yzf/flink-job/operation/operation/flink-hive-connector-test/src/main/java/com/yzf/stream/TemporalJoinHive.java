package com.yzf.stream;

import com.yzf.source.RegisterHiveCatelog;
import com.yzf.source.RegisterProcessTimeFlinkTable;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * Hive维度表join
 */
public class TemporalJoinHive {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hiveConfDir = parameterTool.get("hive_conf_dir","/data/flink/emr_hive_conf/conf");
        // hive提交的时候按照checkpoint的时间进行提交，如果checkpoint太频繁，容易产生小文件过多的问题
        env.enableCheckpointing(1000*10);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);

        RegisterHiveCatelog.register(tableEnv,hiveConfDir);
        RegisterProcessTimeFlinkTable.registerSource(env,tableEnv);

        Table re = tableEnv.sqlQuery("select o.user_id,dim.user_name,o.order_amount,o.dt,o.hr,o.proctime from source_table as o "
                 +
                "left join flink_database.user_info for system_time as of o.proctime as dim on o.user_id = dim.user_id"
                );
//        re.execute().print();
        tableEnv.toAppendStream(re, Row.class).print("单维度表测试 增加事件时间");
        env.execute();


    }
}
