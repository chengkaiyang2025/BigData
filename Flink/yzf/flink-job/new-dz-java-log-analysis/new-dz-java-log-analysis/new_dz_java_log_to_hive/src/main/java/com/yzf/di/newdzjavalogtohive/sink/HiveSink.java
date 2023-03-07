package com.yzf.di.newdzjavalogtohive.sink;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import com.yzf.di.newdzjavalogtohive.util.MyPropertiesUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.IOException;

public class HiveSink<JavaLogBean> {
    public void sink(StreamExecutionEnvironment env, DataStream<com.yzf.di.newdzjavalogtohive.bean.JavaLogBean> toJson, MyPropertiesUtil mp) throws IOException {
        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance()
                .useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, environmentSettings);
        // 1、注册hive
        RegisterHiveCatelog.register(tableEnv,mp.get("sink.hive.conf.dir"));
        // 2、从schema.sql中获得流表的字段，将流转为表
        Table table = tableEnv.fromDataStream(toJson,
                "metadata_topic,\n" +
                        "message,\n" +
                        "source,\n" +
                        "message_date,\n" +
                        "message_tid,\n" +
                        "message_level,\n" +
                        "message_thread,\n" +
                        "message_logger,\n" +
                        "message_logger_for_short,\n" +
                        "message_file,\n" +
                        "message_line,\n" +
                        "fields_module,\n" +
                        "fields_log_item,\n" +
                        "fields_log_es_index,\n" +
                        "pc.proctime as proctime"
                );

        tableEnv.createTemporaryView("source_table",table);

        Table re = tableEnv.sqlQuery("select \n" +
                "metadata_topic,\n" +
                "message,\n" +
                "source,\n" +
                "message_date,\n" +
                "message_tid,\n" +
                "message_level,\n" +
                "message_thread,\n" +
                "message_logger,\n" +
                "message_logger_for_short,\n" +
                "message_file,\n" +
                "message_line,\n" +
                "fields_module,\n" +
                "fields_log_item,\n" +
                "fields_log_es_index,\n" +

                "file_id,\n" + // git提交号',
                "file_name,\n" + //文件名称',
                "file_type,\n" + //文件类型',
                "file_path,\n" + //文件的项目路径',
                "file_mode,\n" + //文件mode',
                "file_last_commit_id,\n" + //文件对应的最新一个提交号',
                "project_id,\n" + //gitlab项目id',
                "project_name,\n" + //gitlab项目名称',
                "project_description,\n" + //gitlab项目描述',
                "project_http_url_to_repo,\n" + //gitlab项目链接',
                "last_commit_info_title,\n" + //最新一个提交信息：主题',
                "last_commit_info_committer_name,\n" + //最新一个提交信息：提交人姓名',
                "last_commit_info_committer_email,\n" + //最新一个提交信息：提交人邮箱',
                "last_commit_info_committed_date,\n" + //最新一个提交信息：提交日期',
                "last_commit_info_commit_link,\n" + //最新一个提交信息：对应的gitlab链接',
                "java_class_path,\n" + //如果是java的文件，完整的java类路径',
                "java_class_path_for_short,\n" + //如果是java的文件，完整的java类路径简称',


                "substr(replace(message_date,'-',''),1,8) as dt\n" +
                "from source_table as s\n"+
                "left join flink_database.dim_git_info for system_time as of s.proctime as d\n" +
                "on s.message_file = d.file_name and s.message_logger_for_short = d.java_class_path_for_short"
                );
        re.printSchema();

//        tableEnv.toAppendStream(re, Row.class).print("table: ");

        re.executeInsert("log_ods.ods_new_dz_java_log_all");
    }
}
