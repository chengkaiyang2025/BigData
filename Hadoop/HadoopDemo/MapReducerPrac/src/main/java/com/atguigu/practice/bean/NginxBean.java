package com.atguigu.practice.bean;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * {"remote_user":"-","fields-set":"set12",
 * "fields-log_item":"access","fields-ip":"172.18.12.1",
 * "fields-apptype":"nginxaccess",
 * "fields-log_es_index":"daizhang-set12-nginx",
 * "source":"\/data\/nginx\/logs\/access.log",
 * "host-name":"set12-nginx",
 * "proxy_add_x_forwarded_for":"221.226.82.254, 100.125.23.176",
 * "nginx":"172.18.12.1","scheme":"http","body_bytes_sent":"0",
 * "time_local":"08\/May\/2021:23:59:55 +0800",
 * "upstream_addr_nm":"0.001",
 * "@timestamp":"2021-05-08T15:59:55.000Z",
 * "beat":{},"http_referer":"https:\/\/daizhang322.yunzhangfang.com\/vt\/stock\/costAccounting?b6fa80247fbf131bc32f019511277bff2021f3a9b561b982a15986b7a66827fc1361d86ae5fb0d75466b384838299a25ce47be4645223692929884d89007d7826e9125f5c6f3c0c018b54aeab1b5c44ff42de9186d627d76",
 * "request":"GET \/vt\/0.71622d37a779fe7d2b71.js HTTP\/1.1","ssl_cipher":"-","http_user_agent":"Mozilla\/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit\/537.36 (KHTML, like Gecko) HeadlessChrome\/90.0.4430.85 Safari\/537.36",
 * "request_time":0.001,"x_forwarded_for":"221.226.82.254",
 * "upstream_addr":"172.18.12.2:3000","request_length":"1312",
 * "http_cookie":"Hm_lvt_5b07def52c26a375c2ec8059668f1c81=1620489503; Hm_lpvt_5b07def52c26a375c2ec8059668f1c81=1620489503; _ga=GA1.2.992679249.1620489503; _gid=GA1.2.1685844453.1620489503; access_token=eyJhbGciOiJIUzI1NiJ9.eyJnc0lkIjoiNjkzMjE4NTA2NzY2MTAyNTI4IiwiaXNzIjoiYXV0aDAiLCJ1c2VyTmFtZSI6ImppYW5nc3UzMjciLCJleHAiOjE2MjA1NTQzMjh9.dZrgtzMWNShKmOZGsmN26Y9ubOlkMsfZuFt0eC5A4Ms; SESSION=dfb10e8f-8760-473e-b792-649ccc0fb343",
 * "remote_addrx":"100.125.23.176",
 * "status":"304"}
 */
public class NginxBean implements Writable {
    private String fields_set;
    private String time_local;
    private String request;
    private Long request_time;
    private Long request_length;
    private String status;
    private String http_cookie;

    public String getHttp_cookie() {
        return http_cookie;
    }

    public void setHttp_cookie(String http_cookie) {
        this.http_cookie = http_cookie;
    }

    public String getFields_set() {
        return fields_set;
    }

    public void setFields_set(String fields_set) {
        this.fields_set = fields_set;
    }

    public String getTime_local() {
        return time_local;
    }

    public void setTime_local(String time_local) {
        this.time_local = time_local;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Long getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Long request_time) {
        this.request_time = request_time;
    }

    public Long getRequest_length() {
        return request_length;
    }

    public void setRequest_length(Long request_length) {
        this.request_length = request_length;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NginxBean() {
    }

    /**
     * @param dataOutput
     *     private String fields_set;
     *     private String time_local;
     *     private String request;
     *     private Long request_time;
     *     private Long request_length;
     *     private String status;
     * @throws IOException
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.fields_set);
        dataOutput.writeUTF(this.time_local);
        dataOutput.writeUTF(this.request);
        dataOutput.writeLong(this.request_time);
        dataOutput.writeLong(this.request_length);
        dataOutput.writeUTF(this.status);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.fields_set = dataInput.readUTF();
        this.time_local = dataInput.readUTF();
        this.request = dataInput.readUTF();
        this.request_time = dataInput.readLong();
        this.request_length = dataInput.readLong();
        this.status = dataInput.readUTF();
    }

    @Override
    public String toString() {
        return "NginxBean{" +
                "fields_set='" + fields_set + '\'' +
                ", time_local='" + time_local + '\'' +
                ", request='" + request + '\'' +
                ", request_time=" + request_time +
                ", request_length=" + request_length +
                ", status='" + status + '\'' +
                '}';
    }
}
