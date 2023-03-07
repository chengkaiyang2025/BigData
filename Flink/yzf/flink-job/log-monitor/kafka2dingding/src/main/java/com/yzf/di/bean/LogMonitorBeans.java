package com.yzf.di.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/9 14:56
 */
public class LogMonitorBeans implements Serializable {
    private static final long serialVersionUID = -1074776348062331911L;
    private List<LogMonitorBean> errorBeans;
    private String type;
    public LogMonitorBeans () {
        errorBeans = new ArrayList<>();
    }

    public LogMonitorBeans(LogMonitorBean logMonitorBean) {
        this.type = logMonitorBean.getType();
        this.errorBeans = Collections.singletonList(logMonitorBean);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LogMonitorBean> getErrorBeans() {
        return errorBeans;
    }

    public void setErrorBeans(List<LogMonitorBean> errorBeans) {
        this.errorBeans = errorBeans;
    }

    public LogMonitorBeans add(LogMonitorBean logMonitorBean) {
        boolean hasThat = false;
        for (LogMonitorBean v1 : this.errorBeans) {
            if (v1.getIp().equalsIgnoreCase(logMonitorBean.getIp())) {
                v1.addAll(logMonitorBean.getErrorLogs());
                hasThat = true;
                break;
            }
        }
        if (!hasThat) {
            this.errorBeans.add(logMonitorBean);
        }

        return this;
    }

    public LogMonitorBeans addLogMonitorBeans(LogMonitorBeans other) {
        for (LogMonitorBean logMonitorBean : other.getErrorBeans()) {
            this.add(logMonitorBean);
        }
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
