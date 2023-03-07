package com.yzf.di.source;

import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class RegisterHiveCatelog {
    public static void register(StreamTableEnvironment tableEnv, String hiveConfDir) {
        String name = "hiveCatalog";
        String defaultDatabase = "default";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir);
        tableEnv.registerCatalog("hiveCatalog", hive);
        tableEnv.useCatalog("hiveCatalog");
    }
}
