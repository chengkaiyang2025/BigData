package com.yzf.bean;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：pojo类，用于保存Nginx日志以及从Cookie中的access_token中解析的信息。
 * @date ：2021/1/27 下午6:45
 */


public class NginxLogFormatter extends TokenInfo {


    // 对方ip
    private String remote_addrx;
    // 访问token
    private String access_token;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
    // 请求url
    private String request_base_url;

    public String getRequest_base_url() {
        return request_base_url;
    }

    public void setRequest_base_url(String request_base_url) {
        this.request_base_url = request_base_url;
    }

    public String getRequest_params() {
        return request_params;
    }

    public void setRequest_params(String request_params) {
        this.request_params = request_params;
    }
    // 请求参数
    private String request_params;

    public String getRequest_web_page() {
        return request_web_page;
    }

    public void setRequest_web_page(String request_web_page) {
        this.request_web_page = request_web_page;
    }

    // 请求页面的名称
    private String request_web_page;
    private String cookie;
    public String getRemote_addrx() {
        return remote_addrx;
    }

    public void setRemote_addrx(String remote_addrx) {
        this.remote_addrx = remote_addrx;
    }

    public String getTime_local() {
        return time_local;
    }

    public void setTime_local(String time_local) {
        this.time_local = time_local;
    }

    @Override
    public String toString() {
        return "NginxLogFormatter" +
                "用户名：" + getUserName() +
                ", 公司id：'" + getGsId() +
//                ", 用户id：" + getUserId() + '\'' +
//                ", 电话：" + get + '\'' +
                ", 所属set：" + getSet() +
                ",请求IP: " + remote_addrx +
//                ", access_token: " + access_token + '\'' +
//                ", tokenInfo=" + tokenInfo +
//                ", cookie: " + cookie + '\'' +
                ", 请求时间: " + time_local +
//                ", timestamp: " + timestamp + '\'' +
//                ", http_referer: " + http_referer + '\'' +
//                ", nginx: " + nginx + '\'' +
//                ", fields_set: " + fields_set + '\'' +
//                ", fields_ip: " + fields_ip + '\'' +
                ", 浏览器UA: " + http_user_agent +
                ", 请求页面: " + request_web_page +
                ", 请求URL: " + request +
//                ", request_time: " + request_time + '\'' +
//                ", status: " + status + '\'' +
//                ", x_forwarded_for: " + x_forwarded_for + '\'' +
//                ", proxy_add_x_forwarded_for: " + proxy_add_x_forwarded_for + '\'' +
                '}';
    }

    // 本地时间
    private String time_local;
    // 访问时间
    private String timestamp;
    // Refer来源信息
    private String http_referer;





    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getNginx() {
        return nginx;
    }

    public void setNginx(String nginx) {
        this.nginx = nginx;
    }

    public String getFields_set() {
        return fields_set;
    }

    public void setFields_set(String fields_set) {
        this.fields_set = fields_set;
    }

    public String getFields_ip() {
        return fields_ip;
    }

    public void setFields_ip(String fields_ip) {
        this.fields_ip = fields_ip;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getX_forwarded_for() {
        return x_forwarded_for;
    }

    public void setX_forwarded_for(String x_forwarded_for) {
        this.x_forwarded_for = x_forwarded_for;
    }

    public String getProxy_add_x_forwarded_for() {
        return proxy_add_x_forwarded_for;
    }

    public void setProxy_add_x_forwarded_for(String proxy_add_x_forwarded_for) {
        this.proxy_add_x_forwarded_for = proxy_add_x_forwarded_for;
    }

    // Nginx地址
    private String nginx;
    // 所属set
    private String fields_set;
    // Set ip
    private String fields_ip;
    // 浏览器头
    private String http_user_agent;
    // 请求url
    private String request;
    // 请求时长
    private String request_time;
    // 响应码
    private String status;
    // 疑似代理ip？
    private String x_forwarded_for;
    // 疑似代理ip？
    private String proxy_add_x_forwarded_for;
}
