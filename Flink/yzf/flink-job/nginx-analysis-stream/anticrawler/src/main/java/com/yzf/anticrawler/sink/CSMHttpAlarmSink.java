package com.yzf.anticrawler.sink;

import com.alibaba.fastjson.JSON;
import com.yzf.anticrawler.bean.CSMRequestBody;
import org.apache.flink.types.Row;
import com.yzf.anticrawler.util.CSMMessageSender;
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



/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：向csm发送http请求，相应发出预警
 * @date ：2021/2/3 下午5:15
 */


public class CSMHttpAlarmSink extends RichSinkFunction<Row> {
    private CloseableHttpClient client;
    @Override
    public void open(Configuration parameters) throws Exception {
        client = HttpClients.createDefault();

    }
    private String csm_alarm_link;

    public CSMHttpAlarmSink(String csm_alarm_link) {
        this.csm_alarm_link = csm_alarm_link;
    }

    @Override
    public void close() throws Exception {

        client.close();
    }

    @Override
    public void invoke(Row row, Context context)  {
        try {

        HttpPost httpPost = new HttpPost(this.csm_alarm_link);

        CSMRequestBody csmRequestBody = new CSMRequestBody(row);

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
