package com.yzf.common;


import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class WebUrlTest {
    @Test
    public void testUrl() {
        System.out.println(WebUrl.urlAndWebPage.getOrDefault("","空"));
        System.out.println(WebUrl.urlAndWebPage.get("/portal/index/book/list"));
        System.out.println(WebUrl.urlAndWebPage.get("/portal/index/book/lis"));
    }
    @Test
    public void testUrl2() {

    }
}