package com.yzf.di.newdzjavalogtohive.util;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import com.yzf.di.newdzjavalogtohive.constants.LogConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecodeJavaLogBeanUtil {
    private static final String regex = "\\[[^\\[]*\\] ";
    private static final String subst = "";
    private static final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    private static final int MATCH_BUCKET_SIZE = 5;
    public static String messageLoggerForShort0(String messageLogger){
        StringBuilder sb = new StringBuilder();
        if(!messageLogger.contains(".")) return "非法类路径";
        String[] split = messageLogger.trim().split("\\.");
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i].substring(0,1));
            if(i < split.length -1 ){
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * TODO 编写这个方法是因为系统运维部的日志规范中，规定了 logger头开启缩写。建议之后联系运维部取消缩写，不再使用这个方法，日志中打印class完整全路径。
     * 与gitlab项目 http://gitlab.yzf.net/group_di/bigdata/fetch-git-info 中弃用的方法类似功能
     * @param logBean
     * @return
     */
    @Deprecated()
    public static JavaLogBean messageLoggerForShort(JavaLogBean logBean){
        try {
            String message_logger = logBean.getMessage_logger();
            logBean.setMessage_logger_for_short(messageLoggerForShort0(message_logger));
        }catch (Exception e){
        }

        return logBean;
    }
    /**
     * 这里暂时使用正则匹配[]关键符号，按照顺序进行匹配：
     * TODO 后续 按照不同的日志头 进行优化
     * 通过正则表达式，将日志字符串进行切割，放到match_bucket中，最多匹配到MATCH_BUCKET_SIZE，防止溢出。
     * @param logBean 待匹配的日志bean
     * @return JavaLogBean 匹配好的日志bean
     */
    @Deprecated
    public static JavaLogBean decode(JavaLogBean logBean){
        String string = logBean.getMessage();

        /**
         * step1. 准备游标
         */
        int idx = 0,lastChar = 0;
        String[] match_bucket = new String[MATCH_BUCKET_SIZE];
        /**
         * step2. 正则匹配[]，按顺序装填到 match_bucket 中。
         * 由于日志消息体中，程序员可能自己打印带有[]符号的日志，所以这里MATCH_BUCKET_SIZE要尽量设置小一些。
         * 原则是，按照顺序，尽量将标准的日志头[]匹配到 match_bucket 中。
         */
        Matcher m = pattern.matcher(string);
        while (m.find() && idx < MATCH_BUCKET_SIZE ) {
            lastChar = m.end();
            match_bucket[idx] = string.substring(m.start(),lastChar).replace("[","").replace("]","").trim();
            idx ++;
        }
        /**
         * step3. 根据match_bucket中的 idx-1 匹配，按照匹配到的个数路由
         */
        switch ( idx ){
            case 4:
                logBean = decode4(logBean,match_bucket,string);
                break;
            case 3:
                logBean = decode3(logBean,match_bucket,string);
                break;
            default:
                break;
        }
        return logBean;
    }
    /**
     * 只处理以下[]为4个的日志头：
     * LogConstant.LOG_LOGBACK_PATTERN1
     * %date [%tid] [%level] [%thread] %logger32 [%file : %line] %msg%n
     * @param logBean 待装填日志头的LogBean
     * @param match_bucket 匹配的日志头
     * @param string 原日志字符
     * @return 待装好日志头的LogBean
     */
    private static JavaLogBean decode4(JavaLogBean logBean,String[] match_bucket,String string){

        //  %date：从第1个字符到第1个”["之间的数据。
        logBean.setMessage_date(string.substring(0,string.indexOf("[")).trim());
        // [%tid]
        logBean.setMessage_tid(match_bucket[0]);
        // [%level]
        logBean.setMessage_level(match_bucket[1]);
        // [%thread]
        logBean.setMessage_thread(match_bucket[2]);

        // [%file
        try {
            logBean.setMessage_file(match_bucket[3].split(":")[0].trim());
        }catch (Exception e){
            logBean.setMessage_file(LogConstant.MISS_MATCH_MESSAGE);
        }
        // %line]
        try {
            logBean.setMessage_line(match_bucket[3].split(":")[1].trim());
        }catch (Exception e){
            logBean.setMessage_line(LogConstant.MISS_MATCH_MESSAGE);
        }

        // %logger
        try {
            logBean.setMessage_logger(
                    // [%thread] match_bucket[2] 到 [%file match_bucket[3] 之间。
                    string.substring(
                            string.indexOf(match_bucket[2]) + match_bucket[2].length() + 1,
                            string.indexOf(match_bucket[3]) - 1)
                            .trim()
            );
        }catch (Exception e){
            // do nothing.
            logBean.setMessage_logger(LogConstant.MISS_MATCH_MESSAGE);
        }
        return logBean;
    }

    /**
     * 只处理以下[]为3个的日志头：
     * LogConstant.LOG_LOGBACK_PATTERN2
     * "%date [%level] [%thread] %logger [%file : %line] [..][...] %msg%n"
     * @param logBean 待装填日志头的LogBean
     * @param match_bucket 匹配的日志头
     * @param string 原日志字符
     * @return 待装好日志头的LogBean
     */
    private static JavaLogBean decode3(JavaLogBean logBean,String[] match_bucket,String string){

        //  %date：从第1个字符到第1个”["之间的数据。
        logBean.setMessage_date(string.substring(0,string.indexOf("[")).trim());
        // [%level]
        logBean.setMessage_level(match_bucket[0]);
        // [%thread]
        logBean.setMessage_thread(match_bucket[1]);

        // [%file
        try {
            logBean.setMessage_file(match_bucket[2].split(":")[0].trim());
        }catch (Exception e){
            logBean.setMessage_file(LogConstant.MISS_MATCH_MESSAGE);
        }
        // %line]
        try {
            logBean.setMessage_line(match_bucket[2].split(":")[1].trim());

        }catch (Exception e){
            logBean.setMessage_line(LogConstant.MISS_MATCH_MESSAGE);
        }

        // %logger
        try {
            logBean.setMessage_logger(
                    // [%thread] match_bucket[1] 到 [%file] match_bucket[2] 之间。
                    string.substring(
                            string.indexOf(match_bucket[1]) + match_bucket[1].length() + 1,
                            string.indexOf(match_bucket[2]) - 1)
                            .trim()
            );
        }catch (Exception e){
            // do nothing.
            logBean.setMessage_logger(LogConstant.MISS_MATCH_MESSAGE);
        }

        return logBean;
    }
}
