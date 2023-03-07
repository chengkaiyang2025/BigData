package com.yzf.di.newdzlogsinkhive.stream;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.util.TimeFormatUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *  清洗json，将json中key带有“-、@“的数据去掉。
 */
public class JsonFormatMapper implements MapFunction<ObjectNode, LogBean> {
    @Override
    public LogBean map(ObjectNode value) throws Exception {
        LogBean logBean = new LogBean();
        try {
            Double request_length = Double.valueOf(value.get("request_length").asText());
//            String fields_set = value.get("fields-set").asText();
            String time_local_ts = TimeFormatUtil.transformer2(value.get("time_local").asText());
            value.put("time_local_ts",time_local_ts);

            value.put("request_length",request_length);
            value.remove("request_length");
            logBean.setUpstream_addr(value.get("upstream_addr").asText());
            logBean.setBody_bytes_sent(value.get("body_bytes_sent").asText());
//            logBean.setSsl_cipher(value.get("ssl_cipher").asText());
            logBean.setSource(value.get("source").asText());
            logBean.setProxy_add_x_forwarded_for(value.get("proxy_add_x_forwarded_for").asText());

            logBean.setRemote_user(value.get("remote_user").asText());
            logBean.setRequest(value.get("request").asText());
            logBean.setRequest_time(value.get("request_time").asDouble());
            logBean.setTime_local(value.get("time_local").asText());
            logBean.setHttp_user_agent(value.get("http_user_agent").asText());

            logBean.setHttp_referer(value.get("http_referer").asText());
            logBean.setRemote_addrx(value.get("remote_addrx").asText());
            logBean.setNginx(value.get("").asText());
            logBean.setScheme(value.get("").asText());
            logBean.setStatus(value.get("status").asText());

            logBean.setUpstream_addr_nm(value.get("upstream_addr_nm").asText());
            logBean.setX_forwarded_for(value.get("x_forwarded_for").asText());


            logBean.setTime_local_ts(value.get("time_local_ts").asText());



        }catch (Exception e){
//            e.printStackTrace();
        }
        return logBean;
    }
}