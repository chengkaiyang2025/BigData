package com.yzf.util;

import com.yzf.bean.RequestCnt;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
public class CSMMessageSenderTest {
    RequestCnt requestCnt = new RequestCnt();

    @Before
    public void makeRequestCnt(){
        requestCnt.setUserName("demo");
        requestCnt.setLastTime("2021-02-01 18:05:00");
        requestCnt.setRequest_cnt(20);
        requestCnt.setGsId("1");
        requestCnt.setRequest_web_page("企业账套信息-基础设置-企业信息-基本信息");

    }
    @Test
    public void getHeaderInfos() {
        System.out.println(CSMMessageSender.getHeaderInfos());

    }

    @Test
    public void getDesc(){
        String desc = CSMMessageSender.getDesc(requestCnt);
        System.out.println(desc);
    }
    @Test
    @Ignore
    public void sendMessage() throws IOException {

//        CSMMessageSender.sendMessage(requestCnt);

    }
}