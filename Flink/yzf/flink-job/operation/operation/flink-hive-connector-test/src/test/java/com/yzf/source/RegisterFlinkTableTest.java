package com.yzf.source;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class RegisterFlinkTableTest {

    @Test
    public void registerSource() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(date));
        System.out.println(date.getHours());
    }
}