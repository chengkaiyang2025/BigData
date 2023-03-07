package com.yzf.di.newdzjavalogtohive.sink;

import com.yzf.di.newdzjavalogtohive.util.MyPropertiesUtil;
import org.apache.flink.table.api.SqlDialect;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

import java.util.Properties;

public class RegisterHiveCatelog {
    public static void register(StreamTableEnvironment tableEnv, String hiveConf){
        String name = "myhive";
        String defaultDatabase = "default";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConf);
        tableEnv.registerCatalog("myhive", hive);
        tableEnv.useCatalog("myhive");
        tableEnv.useDatabase("dwd");
        tableEnv.getConfig().setSqlDialect(SqlDialect.HIVE);
    }
}
