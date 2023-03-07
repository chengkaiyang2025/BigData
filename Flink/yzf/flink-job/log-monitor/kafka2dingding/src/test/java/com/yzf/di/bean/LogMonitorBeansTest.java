package com.yzf.di.bean;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/10 14:59
 */
public class LogMonitorBeansTest {
    @Test
    public void addLogMonitorBeansTest() {
        LogMonitorBean logMonitorBean = new LogMonitorBean();
        logMonitorBean.setIp("172.16.190.35");
        logMonitorBean.setType("logstash_error_log");
        logMonitorBean.addFilePath("kudu_ndz_a_area.log","kudu_ndz_a_area ERROR1...")
                .addFilePath("kudu_ndz_a1_area.log","kudu_ndz_a1_area ERROR1...");
        LogMonitorBeans beans1 = new LogMonitorBeans(logMonitorBean);

        LogMonitorBean logMonitorBean2 = new LogMonitorBean();
        logMonitorBean2.setIp("172.16.190.35");
        logMonitorBean2.setType("logstash_error_log");
        logMonitorBean2.addFilePath("kudu_ndz_a_area.log","kudu_ndz_a_area ERROR2...")
                .addFilePath("kudu_ndz_a1_area.log","kudu_ndz_a1_area ERROR2...");
        LogMonitorBeans beans2 = new LogMonitorBeans(logMonitorBean2);

        LogMonitorBeans add1 = new LogMonitorBeans().addLogMonitorBeans(beans1);
        LogMonitorBeans add = add1.addLogMonitorBeans(beans2);

        Assert.assertEquals(add.getErrorBeans().size(),1);
        Assert.assertEquals(add.getErrorBeans().get(0).getErrorLogs().size(),2);


    }
}
