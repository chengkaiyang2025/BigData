package com.yzf.di.newdzlogsinkhive.util;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonUtilsTest {

    @Test
    public void objectToByte() {
        LogBean logBean = new LogBean();
        logBean.setHttp_user_agent("sdf");
        logBean.setCookie_phone("1212");
        byte[] bytes = JsonUtils.objectToByte(logBean);
        System.out.println(bytes);

        LogBean logBean1 = JsonUtils.ByteToPojo(bytes, LogBean.class);
        System.out.println(logBean1);
    }

    @Test
    public void byteToPojo() {
    }
}