package com.yzf.di.newdzlogsinkhive.bean;


import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogBean {
    private	 String	upstream_addr;
    private	 String	body_bytes_sent;
    private	 String	ssl_cipher;
    private	 String	source;
    private	 String	proxy_add_x_forwarded_for;
    private	 String	remote_user;

    public String getHost_name() {
        return host_name;
    }

    public String getBeat_name() {
        return beat_name;
    }

    public void setBeat_name(String beat_name) {
        this.beat_name = beat_name;
    }

    @Override
    public String toString() {
        return "LogBean{" +
                "upstream_addr='" + upstream_addr + '\'' +
                ", body_bytes_sent='" + body_bytes_sent + '\'' +
                ", ssl_cipher='" + ssl_cipher + '\'' +
                ", source='" + source + '\'' +
                ", proxy_add_x_forwarded_for='" + proxy_add_x_forwarded_for + '\'' +
                ", remote_user='" + remote_user + '\'' +
                ", host_name='" + host_name + '\'' +
                ", beat_name='" + beat_name + '\'' +
                ", request='" + request + '\'' +
                ", request_time=" + request_time +
                ", time_local='" + time_local + '\'' +
                ", http_user_agent='" + http_user_agent + '\'' +
                ", http_referer='" + http_referer + '\'' +
                ", remote_addrx='" + remote_addrx + '\'' +
                ", nginx='" + nginx + '\'' +
                ", scheme='" + scheme + '\'' +
                ", status='" + status + '\'' +
                ", upstream_addr_nm='" + upstream_addr_nm + '\'' +
                ", x_forwarded_for='" + x_forwarded_for + '\'' +
                ", cookie_gs_id='" + cookie_gs_id + '\'' +
                ", cookie_phone='" + cookie_phone + '\'' +
                ", http_cookie='" + http_cookie + '\'' +
                ", cookie_user_id='" + cookie_user_id + '\'' +
                ", cookie_user_name='" + cookie_user_name + '\'' +
                ", ua_browser_version_info='" + ua_browser_version_info + '\'' +
                ", ua_device_type='" + ua_device_type + '\'' +
                ", ua_os_family='" + ua_os_family + '\'' +
                ", ua_os_name='" + ua_os_name + '\'' +
                ", ua_type='" + ua_type + '\'' +
                ", ua_ua_family='" + ua_ua_family + '\'' +
                ", ua_ua_name='" + ua_ua_name + '\'' +
                ", time_local_ts='" + time_local_ts + '\'' +
                '}';
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }
    //host-name为json里面的key，这里通过俩个注解手动绑定
    @JsonProperty("host_name")
    @JsonAlias("host-name")
    private String host_name;
    @JsonProperty("beat_name")
    @JsonAlias("beat-name")
    private String beat_name;
    private	 String	request;
    private  Double	request_time;
    private	 String	time_local;
    private	 String	http_user_agent;
    private	 String	http_referer;
    private	 String	remote_addrx;
    private	 String	nginx;
    private	 String	scheme;
    private	 String	status;
    private	 String	upstream_addr_nm;
    private	 String	x_forwarded_for;
    private	 String	cookie_gs_id;
    private	 String	cookie_phone;

    public String getHttp_cookie() {
        return http_cookie;
    }

    public void setHttp_cookie(String http_cookie) {
        this.http_cookie = http_cookie;
    }

    private String http_cookie;
    public String getUpstream_addr() {
        return upstream_addr;
    }

    public void setUpstream_addr(String upstream_addr) {
        this.upstream_addr = upstream_addr;
    }

    public String getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(String body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public String getSsl_cipher() {
        return ssl_cipher;
    }

    public void setSsl_cipher(String ssl_cipher) {
        this.ssl_cipher = ssl_cipher;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProxy_add_x_forwarded_for() {
        return proxy_add_x_forwarded_for;
    }

    public void setProxy_add_x_forwarded_for(String proxy_add_x_forwarded_for) {
        this.proxy_add_x_forwarded_for = proxy_add_x_forwarded_for;
    }

    public String getRemote_user() {
        return remote_user;
    }

    public void setRemote_user(String remote_user) {
        this.remote_user = remote_user;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Double getRequest_time() {
        return request_time;
    }

    public void setRequest_time(Double request_time) {
        this.request_time = request_time;
    }

    public String getTime_local() {
        return time_local;
    }

    public void setTime_local(String time_local) {
        this.time_local = time_local;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getRemote_addrx() {
        return remote_addrx;
    }

    public void setRemote_addrx(String remote_addrx) {
        this.remote_addrx = remote_addrx;
    }

    public String getNginx() {
        return nginx;
    }

    public void setNginx(String nginx) {
        this.nginx = nginx;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpstream_addr_nm() {
        return upstream_addr_nm;
    }

    public void setUpstream_addr_nm(String upstream_addr_nm) {
        this.upstream_addr_nm = upstream_addr_nm;
    }

    public String getX_forwarded_for() {
        return x_forwarded_for;
    }

    public void setX_forwarded_for(String x_forwarded_for) {
        this.x_forwarded_for = x_forwarded_for;
    }

    public String getCookie_gs_id() {
        return cookie_gs_id;
    }

    public void setCookie_gs_id(String cookie_gs_id) {
        this.cookie_gs_id = cookie_gs_id;
    }

    public String getCookie_phone() {
        return cookie_phone;
    }

    public void setCookie_phone(String cookie_phone) {
        this.cookie_phone = cookie_phone;
    }

    public String getCookie_user_id() {
        return cookie_user_id;
    }

    public void setCookie_user_id(String cookie_user_id) {
        this.cookie_user_id = cookie_user_id;
    }

    public String getCookie_user_name() {
        return cookie_user_name;
    }

    public void setCookie_user_name(String cookie_user_name) {
        this.cookie_user_name = cookie_user_name;
    }

    public String getUa_browser_version_info() {
        return ua_browser_version_info;
    }

    public void setUa_browser_version_info(String ua_browser_version_info) {
        this.ua_browser_version_info = ua_browser_version_info;
    }

    public String getUa_device_type() {
        return ua_device_type;
    }

    public void setUa_device_type(String ua_device_type) {
        this.ua_device_type = ua_device_type;
    }

    public String getUa_os_family() {
        return ua_os_family;
    }

    public void setUa_os_family(String ua_os_family) {
        this.ua_os_family = ua_os_family;
    }

    public String getUa_os_name() {
        return ua_os_name;
    }

    public void setUa_os_name(String ua_os_name) {
        this.ua_os_name = ua_os_name;
    }

    public String getUa_type() {
        return ua_type;
    }

    public void setUa_type(String ua_type) {
        this.ua_type = ua_type;
    }

    public String getUa_ua_family() {
        return ua_ua_family;
    }

    public void setUa_ua_family(String ua_ua_family) {
        this.ua_ua_family = ua_ua_family;
    }

    public String getUa_ua_name() {
        return ua_ua_name;
    }

    public void setUa_ua_name(String ua_ua_name) {
        this.ua_ua_name = ua_ua_name;
    }

    public String getTime_local_ts() {
        return time_local_ts;
    }

    public void setTime_local_ts(String time_local_ts) {
        this.time_local_ts = time_local_ts;
    }

    private	 String	cookie_user_id;
    private	 String	cookie_user_name;
    private	 String	ua_browser_version_info;
    private	 String	ua_device_type;
    private	 String	ua_os_family;
    private	 String	ua_os_name;
    private	 String	ua_type;
    private	 String	ua_ua_family;
    private	 String	ua_ua_name;
    private	 String	time_local_ts;

    public LogBean() {
    }
}
