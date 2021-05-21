package com.atguigu.practice.util;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.practice.bean.NginxBean;

import java.util.Optional;

public class Parser {
    public static void jsonStringToPojo(String jsonString, NginxBean nginxBean){
        JSONObject parse = (JSONObject) JSONObject.parse(jsonString);
        nginxBean.setRequest(Optional.ofNullable(parse.getString("request")).orElse("NULL"));
        nginxBean.setFields_set(Optional.ofNullable(parse.getString("fields-set")).orElse("NULL"));
        nginxBean.setStatus(Optional.ofNullable(parse.getString("status")).orElse("NULL"));
        nginxBean.setTime_local(Optional.ofNullable(parse.getString("time_local")).orElse("NULL"));
        nginxBean.setRequest_length(Optional.ofNullable(parse.getLong("request_length")).orElse(0L));
        nginxBean.setRequest_time(Optional.ofNullable(parse.getLong("request_time")).orElse(0L));
        nginxBean.setHttp_cookie(Optional.ofNullable(parse.getString("http_cookie")).orElse("NULL"));
    }
}
