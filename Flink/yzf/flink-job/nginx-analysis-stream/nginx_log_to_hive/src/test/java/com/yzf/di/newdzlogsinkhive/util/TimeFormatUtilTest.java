package com.yzf.di.newdzlogsinkhive.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class TimeFormatUtilTest {

    @Test
    public void transformer2() {
        String t = "24/Aug/2021:14:06:59 +0800";
        System.out.println(t);
        System.out.println(TimeFormatUtil.transformer2(t));
    }
    @Test
    public void test2() throws ParseException {
        String created = "2021-01-01 00:00:00";
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String CreatedGMT = sdf.format(date1);
        System.out.print(CreatedGMT);

    }
    @Test
    public void test(){
        Date date = new Date(1631656636000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(sdf.format(date));
    }
}