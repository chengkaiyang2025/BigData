package com.yzf.di.newdzjavalogtohive.constants;

/**
 * 相关常量
 */
public interface LogConstant {
    String LOG_LEVEL_SEARCH_KEYWORD_DEBUG = "[DEBUG]";
    String LOG_LEVEL_SEARCH_KEYWORD_TRACE = "[TRACE]";
    String LOG_LEVEL_SEARCH_KEYWORD_INFO = "[INFO]";
    String LOG_LEVEL_SEARCH_KEYWORD_WARN = "[WARN]";
    String LOG_LEVEL_SEARCH_KEYWORD_ERROR = "[ERROR]";

    String LOG_LOGBACK_PATTERN1 = "%date [%tid] [%level] [%thread] %logger [%file : %line] %msg%n";
    String LOG_LOGBACK_PATTERN2 = "%date [%level] [%thread] %logger [%file : %line] [..][...] %msg%n";
    String LOG_SLF4J_PATTERN1 = "%date [%tid] [%level] [%thread] %logger [%file : %line] %msg%n";

    String MISS_MATCH_MESSAGE = "日志格式不符合系统运维部的规范，无法解析";
}
