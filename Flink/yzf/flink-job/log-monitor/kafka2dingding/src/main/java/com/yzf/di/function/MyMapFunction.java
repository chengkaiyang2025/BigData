package com.yzf.di.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzf.di.bean.LogMonitorBean;
import org.apache.flink.api.common.functions.MapFunction;

import static com.yzf.di.bean.Kafka2DingDingCommon.*;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/9 16:17
 */
public class MyMapFunction implements MapFunction<String, LogMonitorBean> {
    @Override
    public LogMonitorBean map(String s) throws Exception {
        JSONObject logs = JSON.parseObject(s);
        LogMonitorBean result = new LogMonitorBean();
        result.setType(logs.getJSONObject(KAFKA_MESSAGE_KEY_FIELDS).getString(KAFKA_MESSAGE_KEY_SERVICE));
        result.setIp(logs.getJSONArray(KAFKA_MESSAGE_KEY_TAGS).getString(0));

        result.addFilePath(logs.getJSONObject(KAFKA_MESSAGE_KEY_LOG).getJSONObject(KAFKA_MESSAGE_KEY_FILE).getString(KAFKA_MESSAGE_KEY_PATH),
                logs.getString(KAFKA_MESSAGE_KEY_MESSAGE));
        return result;
    }
}
