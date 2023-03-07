package com.yzf.di.udf;

import org.apache.flink.table.functions.ScalarFunction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/28 15:44
 * @description：
 */
public class TimeFunction {
    public static class UDTimeFormat2UnixTime extends ScalarFunction {
        public Long eval(String time) {
            if (time != null) {
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss z", Locale.ENGLISH);
                try {
                    Date dateTrans = sourceDateFormat.parse(time.replace("GMT", "").replaceAll("\\(.*\\)", ""));
                    return dateTrans.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new Date(0).getTime();
                }
            } else {
                return System.currentTimeMillis();
            }

        }
    }
}
