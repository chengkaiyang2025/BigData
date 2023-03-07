package com.yzf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：时间格式转换工具类
 * @date ：2021/1/31 上午10:49
 */


public class TimeFormatUtil {
    public static String transformer(String time){
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss z", Locale.ENGLISH);
        // 请勿调整targetDateFormat，后续会根据这个
        SimpleDateFormat targetDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateTrans = sourceDateFormat.parse(time.replace("GMT", "").replaceAll("\\(.*\\)", ""));

            return targetDateFormat.format(dateTrans);

        } catch (Exception e) {
            e.printStackTrace();
            return targetDateFormat.format(new Date(0));
        }
    }
}
