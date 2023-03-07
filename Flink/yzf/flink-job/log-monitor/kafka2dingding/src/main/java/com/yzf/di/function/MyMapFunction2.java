package com.yzf.di.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzf.di.bean.LogMonitorBean;
import com.yzf.di.bean.LogMonitorBeans;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;

import static com.yzf.di.bean.Kafka2DingDingCommon.*;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/9 18:00
 */
public class MyMapFunction2 implements MapFunction<String, LogMonitorBeans> {
    @Override
    public LogMonitorBeans map(String value) throws Exception {
        JSONObject logs = JSON.parseObject(value);
        LogMonitorBean result = new LogMonitorBean();
        result.setType(logs.getJSONObject(KAFKA_MESSAGE_KEY_FIELDS).getString(KAFKA_MESSAGE_KEY_SERVICE));
        result.setIp(logs.getJSONArray(KAFKA_MESSAGE_KEY_TAGS).getString(0));

        result.addFilePath(logs.getJSONObject(KAFKA_MESSAGE_KEY_LOG).getJSONObject(KAFKA_MESSAGE_KEY_FILE).getString(KAFKA_MESSAGE_KEY_PATH),
                StringUtils.substring(logs.getString(KAFKA_MESSAGE_KEY_MESSAGE),0,500));

        return new LogMonitorBeans(result);
    }
}
