package com.atguigu.flink.util;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class TimeFormatTest {

    @Test
    public void convert() {
        System.out.println(TimeFormat.convert(1615154118000L));
        System.out.println(TimeFormat.convert(Calendar.getInstance().getTime().getTime()));
    }
    @Test
    public void convert2() {

        try {
            System.out.println(TimeFormat.convert("2021-03-08 05:55:18").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}