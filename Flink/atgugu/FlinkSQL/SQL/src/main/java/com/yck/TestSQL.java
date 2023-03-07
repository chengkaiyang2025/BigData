package com.yck;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class TestSQL {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setInteger("table.exec.resource.default-parallelism",1);
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                //.inBatchMode()
                .withConfiguration(configuration)
                .build();

        TableEnvironment tableEnv = TableEnvironment.create(settings);
        tableEnv.executeSql("CREATE TABLE SourceTable (f0 String) with ('connector' = 'datagen','rows-per-second' = '1')");
//        tableEnv.executeSql("CREATE TABLE SinkTable (product STRING) WITH ('connector' = 'print') ");
        tableEnv.executeSql("CREATE TEMPORARY TABLE SinkTable WITH ('connector' = 'print') LIKE SourceTable (EXCLUDING OPTIONS) ");
        tableEnv.executeSql("INSERT INTO SinkTable SELECT f0 FROM SourceTable");
    }
}
