package com.yzf.di.bean;

/**
 * 发送告警类型： 钉钉、e-mail
 * @Author: MaJiBin
 * @Date: 2021/8/9 11:04
 */
public enum AlertTyre {
    DINGDING ("dingding"),
    EMAIL("email");
    private final String alertTyre;
    AlertTyre (String alertTyre) {
        this.alertTyre = alertTyre;
    }

    public String getAlertTyre() {
        return alertTyre;
    }
}
