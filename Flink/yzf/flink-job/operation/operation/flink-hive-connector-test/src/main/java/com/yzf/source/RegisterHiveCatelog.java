package com.yzf.source;

import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class RegisterHiveCatelog {
    public static void register(StreamTableEnvironment tableEnv,String hiveConfDir){
        String name = "myhive";
        String defaultDatabase = "default";
//        String hiveConfDir = "flink-hive-connector-test/conf"; // a local path
//        String version = "2.1.1";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir);
        tableEnv.registerCatalog("myhive", hive);
        tableEnv.useCatalog("myhive");
        tableEnv.useDatabase("dwd");
        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);
    }
}
