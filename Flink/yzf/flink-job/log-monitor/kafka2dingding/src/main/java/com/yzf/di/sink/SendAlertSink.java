package com.yzf.di.sink;

import com.alibaba.fastjson.JSONObject;
import com.yzf.di.Kafka2DingDingMain;
import com.yzf.di.alert.integration.client.AlertReport;
import com.yzf.di.alert.integration.common.template.AlertTemplateContext;
import com.yzf.di.bean.LogMonitorBean;
import com.yzf.di.bean.LogMonitorBeans;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * @Author: MaJiBin
 * @Date: 2021/8/5 14:21
 */
public class SendAlertSink extends RichSinkFunction<LogMonitorBeans> {
    private static final Logger logger = LoggerFactory.getLogger(SendAlertSink.class);
    private final String postURL;
    private boolean isTest;
    private String alertTyre;

    public SendAlertSink(String postURL, String alertTyre, boolean isTest) {
        this.postURL = postURL;
        this.isTest = isTest;
        this.alertTyre = alertTyre;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // for test
        if (isTest) {
            AlertTemplateContext.getInstance().reload("D:/code/git/flink-job/log-monitor/kafka2dingding/src/main/resources/");
        }
    }

    @Override
    public void invoke(LogMonitorBeans value, Context context) throws Exception {
        super.invoke(value, context);
        if (value.getErrorBeans().isEmpty()) {
            logger.warn("ErrorLogs is null");
            return;
        }

        logger.info("Send alert type: {}",genTemplateName(value.getType(),alertTyre));
        if (isTest) {
            System.out.println(genTemplateName(value.getType(),alertTyre));
            // for test
            System.out.println(AlertReport.sendByLocal(genTemplateName(value.getType(),alertTyre), value));
        } else {
            Map<?, ?> map = AlertReport.sendByServer(postURL + genTemplateName(value.getType(), alertTyre), value);
            if (map == null) {
                logger.error("Send alert Failed!");
            }
        }

    }

    /**
     * 拼接 告警 Template 名
     * @param logType
     * @param alertTyre
     * @return logType + "-" + alertTyre
     */
    private String genTemplateName(String logType,String alertTyre) {
        return logType + "-" + alertTyre;
    }
}
