package com.yzf.di.kuducdc.cdc.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class OctopusDataValidatorTest {

    @Test
    public void jsonSchemaValidator() {
    }

    @Test
    public void dateValidator() {
        System.out.println(OctopusDataValidator.DateValidator("2021", "1"));
        assertEquals("202101",OctopusDataValidator.DateValidator("2021", "1"));
        assertEquals("202102",OctopusDataValidator.DateValidator("2021", "2"));
        assertEquals("202110",OctopusDataValidator.DateValidator("2021", "10"));
        assertEquals("202111",OctopusDataValidator.DateValidator("2021", "11"));
        assertEquals("202112",OctopusDataValidator.DateValidator("2021", "12"));
    }


    @Test
    public void test2()
    {
    }
}