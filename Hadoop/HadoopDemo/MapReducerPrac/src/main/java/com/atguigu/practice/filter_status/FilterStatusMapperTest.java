package com.atguigu.practice.filter_status;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterStatusMapperTest {
    @Test
    public void testJson(){
        String jsonString = "{\"remote_user\":\"-\",\"fields-set\":\"set12\",\"fields-log_item\":\"access\",\"fields-ip\":\"172.18.12.1\",\"fields-apptype\":\"nginxaccess\",\"fields-log_es_index\":\"daizhang-set12-nginx\",\"source\":\"\\/data\\/nginx\\/logs\\/access.log\",\"host-name\":\"set12-nginx\",\"proxy_add_x_forwarded_for\":\"221.226.82.254, 100.125.23.176\",\"nginx\":\"172.18.12.1\",\"scheme\":\"http\",\"body_bytes_sent\":\"0\",\"time_local\":\"08\\/May\\/2021:23:59:55 +0800\",\"upstream_addr_nm\":\"0.001\",\"@timestamp\":\"2021-05-08T15:59:55.000Z\",\"beat\":{},\"http_referer\":\"https:\\/\\/daizhang322.yunzhangfang.com\\/vt\\/stock\\/costAccounting?b6fa80247fbf131bc32f019511277bff2021f3a9b561b982a15986b7a66827fc1361d86ae5fb0d75466b384838299a25ce47be4645223692929884d89007d7826e9125f5c6f3c0c018b54aeab1b5c44ff42de9186d627d76\",\"request\":\"GET \\/vt\\/0.71622d37a779fe7d2b71.js HTTP\\/1.1\",\"ssl_cipher\":\"-\",\"http_user_agent\":\"Mozilla\\/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit\\/537.36 (KHTML, like Gecko) HeadlessChrome\\/90.0.4430.85 Safari\\/537.36\",\"request_time\":0.001,\"x_forwarded_for\":\"221.226.82.254\",\"upstream_addr\":\"172.18.12.2:3000\",\"request_length\":\"1312\",\"http_cookie\":\"Hm_lvt_5b07def52c26a375c2ec8059668f1c81=1620489503; Hm_lpvt_5b07def52c26a375c2ec8059668f1c81=1620489503; _ga=GA1.2.992679249.1620489503; _gid=GA1.2.1685844453.1620489503; access_token=eyJhbGciOiJIUzI1NiJ9.eyJnc0lkIjoiNjkzMjE4NTA2NzY2MTAyNTI4IiwiaXNzIjoiYXV0aDAiLCJ1c2VyTmFtZSI6ImppYW5nc3UzMjciLCJleHAiOjE2MjA1NTQzMjh9.dZrgtzMWNShKmOZGsmN26Y9ubOlkMsfZuFt0eC5A4Ms; SESSION=dfb10e8f-8760-473e-b792-649ccc0fb343\",\"remote_addrx\":\"100.125.23.176\",\"status\":\"304\"}\n";
        JSONObject parse = (JSONObject) JSONObject.parse(jsonString);
        NginxBean nginxBean = parse.toJavaObject(NginxBean.class);
        System.out.println(nginxBean);
    }
}