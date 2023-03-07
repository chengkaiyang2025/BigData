package com.yzf.sink;

import com.alibaba.fastjson.JSON;
import com.yzf.bean.CSMRequestBody;
import com.yzf.bean.RequestCnt;
import com.yzf.util.CSMMessageSender;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import static com.yzf.util.CSMMessageSender.getDesc;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：向csm发送http请求，相应发出预警
 * @date ：2021/2/3 下午5:15
 */


public class CSMHttpAlarmSink extends RichSinkFunction<RequestCnt> {
    private CloseableHttpClient client;
    @Override
    public void open(Configuration parameters) throws Exception {
        client = HttpClients.createDefault();

    }

    @Override
    public void close() throws Exception {

        client.close();
    }

    @Override
    public void invoke(RequestCnt requestCnt, Context context)  {
        // TODO shoud try catch error,or the Flink job will be shutdown.
        try {

        HttpPost httpPost = new HttpPost(com.yzf.config.Configuration.PROPERTIES.getProperty("csm_alarm_link"));

        CSMRequestBody csmRequestBody = new CSMRequestBody("按用户、接口维度统计访问频率",getDesc(requestCnt), new BigInteger(requestCnt.getGsId()),requestCnt.getLastTime());

        String json = JSON.toJSONString(csmRequestBody);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
        Map<String,String> csmHeader = CSMMessageSender.getHeaderInfos();
        for(String key:csmHeader.keySet()){
            httpPost.setHeader(key,csmHeader.get(key));
        }

        CloseableHttpResponse response = null;
        response = client.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
