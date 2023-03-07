package com.yzf.di;

import com.alibaba.fastjson.JSONObject;
import com.yzf.di.alert.integration.client.AlertReport;
import com.yzf.di.alert.integration.common.template.AlertTemplateContext;
import com.yzf.di.bean.LogMonitorBean;
import com.yzf.di.bean.LogMonitorBeans;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/5 15:47
 */
public class Kafka2DingDingTest {
    @Test
    public void test() throws Exception {
        AlertTemplateContext.getInstance().reload("D:/code/git/flink-job/log-monitor/kafka2dingding/src/main/resources/");
        LogMonitorBean logMonitorBean = new LogMonitorBean();
        logMonitorBean.setIp("172.16.190.35");
        logMonitorBean.setType("logstash_error_log");
        logMonitorBean.addFilePath("/data/service/canal/canal-server/logs/kudu_ndz_a_area/kudu_ndz_a_area.log","ERROR...")
                .addFilePath("/data/service/canal/canal-server/logs/kudu_ndz_a1_area/kudu_ndz_a1_area.log","ERROR...");
        LogMonitorBeans logMonitorBeans = new LogMonitorBeans();
        logMonitorBeans.add(logMonitorBean);
//        AlertReport.sendByLocal("canal_error_log-email", logMonitorBeans);
    }
}
