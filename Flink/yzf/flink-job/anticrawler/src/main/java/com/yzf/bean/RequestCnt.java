package com.yzf.bean;

import java.util.List;
import java.util.Set;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：pojo类，用于保存统计结果。
 * @date ：2021/1/27 下午8:32
 */


public class RequestCnt extends NginxLogFormatter {


    @Override
    public String toString() {
        return "请求访问频率：" +
                "登陆用户名称："+getUserName()+
                " 所属代帐公司id："+getGsId()+
                " 请求ip:" + remote_addrx + '\'' +
//                "请求路径" + request_path +
                " 当前时间：" + lastTime +
                " UA种类数量:" + UA_CNT +
                " 请求页面："+getRequest_web_page()+
                " 请求数量:" + request_cnt ;
//                "UA种类" + UA_SET;
    }
    private String lastTime;
    private String remote_addrx;

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    private Integer request_cnt;
    private List<String> request_path;

    public Integer getUA_CNT() {
        return UA_CNT;
    }

    public void setUA_CNT(Integer UA_CNT) {
        this.UA_CNT = UA_CNT;
    }

    private Integer UA_CNT;
    private Set<String> UA_SET;

    public String getRemote_addrx() {
        return remote_addrx;
    }

    public void setRemote_addrx(String remote_addrx) {
        this.remote_addrx = remote_addrx;
    }

    public Integer getRequest_cnt() {
        return request_cnt;
    }

    public void setRequest_cnt(Integer request_cnt) {
        this.request_cnt = request_cnt;
    }

    public List<String> getRequest_path() {
        return request_path;
    }

    public void setRequest_path(List<String> request_path) {
        this.request_path = request_path;
    }


    public Set<String> getUA_SET() {
        return UA_SET;
    }

    public void setUA_SET(Set<String> UA_SET) {
        this.UA_SET = UA_SET;
    }
}
