package com.atguigu.flink.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/9 上午10:36
 */


public class TimeFormat {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String convert(Long ts){
        Date date = new Date(ts);
        String format = sdf.format(date);
        return format;
    }
    public static Long convert(String ts) throws ParseException {
        Date parse = sdf.parse(ts);
        return parse.getTime();
    }
}
