package com.yzf.di.bean;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/9 9:51
 */
public class LogMonitorBeanTest {
    LogMonitorBean logMonitorBean = new LogMonitorBean("172.16.1.109", "canal_error_log");

    String filePath1 = "/data/service/canal/canal-server/logs/kudu_ndz_a_area/kudu_ndz_a_area.log";
    String filePath2 = "/data/service/canal/canal-server/logs/kudu_ndz_c_area/kudu_ndz_c_area.log";

    String errorLog1 = "2021-08-06 11:46:37.658 [Druid-ConnectionPool-Create-448638707] ERROR com.alibaba.druid.pool.DruidDataSource";
    String errorLog2 = "2021-08-07 12:16:37.158 [Druid-ConnectionPool-Create-448638707] ERROR com.alibaba.druid.pool.DruidDataSource";

    @Test
    public void addFilePathTest() {
        logMonitorBean.addFilePath(filePath1, errorLog1)
                .addFilePath(filePath1, errorLog2)
                .addFilePath(filePath2, errorLog1);

        List<String> strings = logMonitorBean.getErrorLogs().get(filePath1);

        Assert.assertEquals(logMonitorBean.getErrorLogs().size(), 2);
        Assert.assertEquals(strings.size(), 2);
    }

    @Test
    public void addLogListTest() {
        List<String> errorLogs1 = Arrays.asList(errorLog1);
        List<String> errorLogs2 = Arrays.asList(errorLog2);
        logMonitorBean.addLogList(filePath1,errorLogs1)
                .addLogList(filePath1,errorLogs2)
                .addLogList(filePath2,errorLogs1);

        List<String> strings = logMonitorBean.getErrorLogs().get(filePath1);

        Assert.assertEquals(logMonitorBean.getErrorLogs().size(), 2);
        Assert.assertEquals(strings.size(), 2);
    }

    @Test
    public void addAllTest() {
        logMonitorBean.addFilePath(filePath1, errorLog1)
                .addFilePath(filePath1, errorLog2);

        LogMonitorBean logMonitorBean2 = new LogMonitorBean("172.16.1.109", "canal_error_log")
            .addFilePath(filePath2, errorLog1);

        logMonitorBean.addAll(logMonitorBean2.getErrorLogs());

        List<String> strings = logMonitorBean.getErrorLogs().get(filePath1);

        Assert.assertEquals(logMonitorBean.getErrorLogs().size(), 2);
        Assert.assertEquals(strings.size(), 2);
    }
}
