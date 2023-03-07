package com.yzf.di.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/5 11:54
 */
public class LogMonitorBean implements Serializable {
    private static final long serialVersionUID = 2626603493875298833L;

    private String ip;
    private String type;
    private Map<String, List<String>> errorLogs;

    public LogMonitorBean(String ip, String type) {
        this.ip = ip;
        this.type = type;
        this.errorLogs = new ConcurrentHashMap<>();
    }

    public LogMonitorBean() {
        this.errorLogs = new ConcurrentHashMap<>();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, List<String>> getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(Map<String, List<String>> errorLogs) {
        this.errorLogs = errorLogs;
    }

    public LogMonitorBean addFilePath(String filePath,String log) {
        List<String> logs = errorLogs.get(filePath) == null ? new ArrayList<String>() : errorLogs.get(filePath);
        logs.add(log);
        errorLogs.put(filePath,logs);
        return this;
    }

    /**
     * 如果已存在 filePath,则直接添加。如果不存在，则添加新的key
     * @param filePath
     * @param logList
     * @return
     */
    public LogMonitorBean addLogList (String filePath,List<String> logList) {
        List<String> logs = errorLogs.get(filePath) == null ? new ArrayList<String>() : errorLogs.get(filePath);
        logs.addAll(logList);
        errorLogs.put(filePath,logs);
        return this;
    }

    public LogMonitorBean addAll(Map<String, List<String>> other) {
        Set<Map.Entry<String, List<String>>> entrySet = other.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            this.addLogList(entry.getKey(),entry.getValue());
        }
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
