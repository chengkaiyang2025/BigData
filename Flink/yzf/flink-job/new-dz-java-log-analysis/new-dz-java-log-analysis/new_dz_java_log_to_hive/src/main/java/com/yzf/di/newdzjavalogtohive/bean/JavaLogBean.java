package com.yzf.di.newdzjavalogtohive.bean;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Documented;
import java.util.Map;
import java.util.Optional;

/**
 * 嵌套的json解析参考：
 * https://www.baeldung.com/jackson-nested-values
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JavaLogBean {
    private String metadata_topic;

    @Override
    public String toString() {
        return "JavaLogBean{" +
                "metadata_topic='" + metadata_topic + '\'' +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                ", message_date='" + message_date + '\'' +
                ", message_tid='" + message_tid + '\'' +
                ", message_level='" + message_level + '\'' +
                ", message_thread='" + message_thread + '\'' +
                ", message_logger='" + message_logger + '\'' +
                ", message_logger_for_short='" + message_logger_for_short + '\'' +
                ", message_file='" + message_file + '\'' +
                ", message_line='" + message_line + '\'' +
                ", fields_module='" + fields_module + '\'' +
                ", fields_log_item='" + fields_log_item + '\'' +
                ", fields_log_es_index='" + fields_log_es_index + '\'' +
                '}';
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public String getMessage_tid() {
        return message_tid;
    }

    public void setMessage_tid(String message_tid) {
        this.message_tid = message_tid;
    }

    public String getMessage_level() {
        return message_level;
    }

    public void setMessage_level(String message_level) {
        this.message_level = message_level;
    }

    public String getMessage_thread() {
        return message_thread;
    }

    public void setMessage_thread(String message_thread) {
        this.message_thread = message_thread;
    }



    public String getMessage_file() {
        return message_file;
    }

    public void setMessage_file(String message_file) {
        this.message_file = message_file;
    }

    public String getMessage_line() {
        return message_line;
    }

    public void setMessage_line(String message_line) {
        this.message_line = message_line;
    }

//    public String getMessage_msg() {
//        return message_msg;
//    }

//    public void setMessage_msg(String message_msg) {
//        this.message_msg = message_msg;
//    }

    private String message;
    private String source;
    /**
     * message_为前缀的，都是解析日志字符串后的格式
     * 日志pattern为 %date [%tid] [%level] [%thread] %logger [%file : %line] %msg%n
     * 命名规范按照logback为准
     */
    private String message_date;
    private String message_tid;
    private String message_level;

    public String getMessage_logger_for_short() {
        return message_logger_for_short;
    }

    public void setMessage_logger_for_short(String message_logger_for_short) {
        this.message_logger_for_short = message_logger_for_short;
    }

    private String message_thread;
    private String message_logger;
    private String message_logger_for_short;
    private String message_file;
    private String message_line;
    // msg不解析
//    private String message_msg;

    public String getMetadata_topic() {
        return metadata_topic;
    }

    public void setMetadata_topic(String metadata_topic) {
        this.metadata_topic = metadata_topic;
    }

    public JavaLogBean() {
    }
    @JsonProperty("fields")
    private void unpackNestedField(Map<String,Object> field){
        this.fields_module = String.valueOf(Optional.ofNullable(field.get("module")).orElse("module"));
        this.fields_log_item = String.valueOf(Optional.ofNullable(field.get("log_item")).orElse("log_item"));
        this.fields_log_es_index = String.valueOf(Optional.ofNullable(field.get("log_es_index")).orElse("log_es_index"));
    }

    public String getMessage_logger() {
        return message_logger;
    }

    public void setMessage_logger(String message_logger) {
        this.message_logger = message_logger;
    }

    @JsonProperty("@metadata")
    private void unpackNestedMetadata(Map<String,Object> metadata){
        this.metadata_topic = String.valueOf(Optional.ofNullable(metadata.get("topic")).orElse("module"));
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }



    public String getFields_module() {
        return fields_module;
    }

    public void setFields_module(String fields_module) {
        this.fields_module = fields_module;
    }

    public String getFields_log_item() {
        return fields_log_item;
    }

    public void setFields_log_item(String fields_log_item) {
        this.fields_log_item = fields_log_item;
    }

    public String getFields_log_es_index() {
        return fields_log_es_index;
    }

    public void setFields_log_es_index(String fields_log_es_index) {
        this.fields_log_es_index = fields_log_es_index;
    }


    private String fields_module;
    private String fields_log_item;
    private String fields_log_es_index;
}
