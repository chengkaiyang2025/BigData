package com.yzf.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeFormatUtilTest {

    @Test
    public void translateSuccee() {
        System.out.println(TimeFormatUtil.transformer("31/Jan/2021:15:55:22 +0800"));
    }

    @Test
    public void translateFail() {
        System.out.println(TimeFormatUtil.transformer("31:Jan/2021:15:55:22 +0800"));
    }
}