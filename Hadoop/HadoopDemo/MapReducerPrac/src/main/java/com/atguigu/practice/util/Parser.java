package com.atguigu.practice.util;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.practice.bean.NginxBean;

public class Parser {
    public static void jsonStringToPojo(String jsonString, NginxBean nginxBean){
        JSONObject parse = (JSONObject) JSONObject.parse(jsonString);
        nginxBean.setRequest(parse.getString("request"));
        nginxBean.setFields_set(parse.getString("fields-set"));
        nginxBean.setStatus(parse.getString("status"));
        nginxBean.setTime_local(parse.getString("time_local"));
        nginxBean.setRequest_length(parse.getLong("request_length"));
        nginxBean.setRequest_time(parse.getLong("request_time"));
    }
}
