package com.yzf.di.newdzlogsinkhive.sink;

import com.google.common.io.Resources;
import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.source.RegisterHiveCatelog;
import org.apache.flink.types.Row;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HiveSink {
    public void sink(StreamExecutionEnvironment env, DataStream<LogBean> toJson, String hiveConfDir) throws IOException {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        // 1、注册hive
        RegisterHiveCatelog.register(tableEnv, hiveConfDir);
        // 2、从schema.sql中获得流表的字段，将流转为表
        Table table = tableEnv.fromDataStream(toJson,
                "upstream_addr,\n" +
                        "body_bytes_sent,\n" +
                        "ssl_cipher,\n" +
                        "source,\n" +
                        "proxy_add_x_forwarded_for,\n" +
                        "remote_user,\n" +
                        "request,\n" +
                        "request_time,\n" +
                        "time_local,\n" +
                        "http_user_agent,\n" +
                        "http_referer,\n" +
                        "remote_addrx,\n" +
                        "nginx,\n" +
                        "scheme,\n" +
                        "status,\n" +
                        "upstream_addr_nm,\n" +
                        "x_forwarded_for,\n" +
                        "cookie_gs_id,\n" +
                        "cookie_phone,\n" +
                        "cookie_user_id,\n" +
                        "cookie_user_name,\n" +
                        "ua_browser_version_info,\n" +
                        "ua_device_type,\n" +
                        "ua_os_family,\n" +
                        "ua_os_name,\n" +
                        "ua_type,\n" +
                        "ua_ua_family,\n" +
                        "ua_ua_name,\n" +
                        "time_local_ts,\n" +
                        "pc.proctime as proctime"
        );

        tableEnv.createTemporaryView("source_table", table);

        Table re = tableEnv.sqlQuery("select \n" +
                "    upstream_addr,\n" +
                "    body_bytes_sent,\n" +
                "    ssl_cipher,\n" +
                "    source,\n" +
                "    proxy_add_x_forwarded_for,\n" +
                "    remote_user,\n" +
                "    request,\n" +
                "    request_time,\n" +
                "    time_local,\n" +
                "    http_user_agent,\n" +
                "    http_referer,\n" +
                "    remote_addrx,\n" +
                "    nginx,\n" +
                "    scheme,\n" +
                "    status,\n" +
                "    upstream_addr_nm,\n" +
                "    x_forwarded_for,\n" +
                "    cookie_gs_id,\n" +
                "    cookie_phone,\n" +
                "    cookie_user_id,\n" +
                "    cookie_user_name,\n" +
                "    ua_browser_version_info,\n" +
                "    ua_device_type,\n" +
                "    ua_os_family,\n" +
                "    ua_os_name,\n" +
                "    ua_type,\n" +
                "    ua_ua_family,\n" +
                "    ua_ua_name,\n" +
                "    time_local_ts,\n" +
                "    gsmc,\n" +
                "    gslx,\n" +
                "    substr(time_local_ts,1,8) as dt\n" +
                "from source_table as s\n" +
                "left join flink_database.dim_dzgs_d for system_time as of s.proctime as d\n" +
                "on s.cookie_gs_id = d.gsid");
        re.printSchema();

//        tableEnv.toAppendStream(re, Row.class).print("table: ");

        re.executeInsert("log_ods.ods_new_dz_nginx_log_all");
    }
}
