package com.atguigu.practice.util;

import com.atguigu.practice.bean.NginxBean;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    private NginxBean nginxBean = new NginxBean();

    private String jsonString = "{\"remote_user\":\"-\",\"fields-set\":\"set11\",\"fields-log_item\":\"access\",\"fields-ip\":\"172.18.11.1\",\"fields-apptype\":\"nginxaccess\",\"fields-log_es_index\":\"daizhang-set11-nginx\",\"source\":\"\\/data\\/nginx\\/logs\\/access.log\",\"host-name\":\"set11-nginx\",\"proxy_add_x_forwarded_for\":\"60.10.169.32, 100.125.23.191\",\"nginx\":\"172.18.11.1\",\"scheme\":\"http\",\"body_bytes_sent\":\"238\",\"time_local\":\"09\\/May\\/2021:00:00:00 +0800\",\"upstream_addr_nm\":\"0.108\",\"@timestamp\":\"2021-05-08T16:00:00.000Z\",\"beat\":{},\"http_referer\":\"-\",\"request\":\"POST \\/task\\/api\\/task\\/getTasks\\/v2?access_token=eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzMTEiLCJwaG9uZSI6IjE4MDMzNjkzNjM1IiwiZ3NJZCI6IjU5MjcxNDI5OTIxNTI2NTc5MiIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJsdW1lbmciLCJleHAiOjE2MjA0OTEzMjgsInVzZXJJZCI6IjU5Mjc0ODI3NzAxMDg5NDg0OCJ9.FX9hV91Ov8_6FQI_Y5-Mka-RsZorddegzHttjvkONJE HTTP\\/1.1\",\"ssl_cipher\":\"-\",\"http_user_agent\":\"libcurl-agent\\/1.0\",\"request_time\":0.108,\"x_forwarded_for\":\"60.10.169.32\",\"upstream_addr\":\"172.18.11.26:8082\",\"request_length\":\"1228\",\"http_cookie\":\"5fdd2c8201364d71a1123fb103c23a30=WyIzMTM2OTYxMDU1Il0\",\"remote_addrx\":\"100.125.23.191\",\"status\":\"200\"}\n";
    @Before
    public void setup(){

    }
    @Test
    public void jsonStringToPojo() {
        Parser.jsonStringToPojo(jsonString,nginxBean);
        System.out.println(nginxBean);
    }

    @Test
    public void t(){
        long size = 2147483647;
    }
}