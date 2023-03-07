package com.yzf.di.newdzlogsinkhive.bean;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：用户的ua信息
 * @date ：2021/2/22 上午11:35
 */


public class UserAgent {
    private String osFamily;
    private String osName;
    private String uaFamily;
    private String browserVersionInfo;

    @Override
    public String toString() {
        return "UserAgent{" +
                "操作系统名称='" + osFamily + '\'' +
                ", 操作系统='" + osName + '\'' +
                ", 浏览器名称='" + uaFamily + '\'' +
                ", 浏览器版本='" + browserVersionInfo + '\'' +
                ", 设备类型='" + deviceType + '\'' +
                ", 浏览器='" + uaName + '\'' +
                ", 类型='" + type + '\'' +
                '}';
    }

    public UserAgent(String osFamily, String osName, String uaFamily, String browserVersionInfo, String deviceType, String uaName, String type) {
        this.osFamily = osFamily;
        this.osName = osName;
        this.uaFamily = uaFamily;
        this.browserVersionInfo = browserVersionInfo;
        this.deviceType = deviceType;
        this.uaName = uaName;
        this.type = type;
    }

    public UserAgent() {
    }

    private String deviceType;
    private String uaName;
    private String type;

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getUaFamily() {
        return uaFamily;
    }

    public void setUaFamily(String uaFamily) {
        this.uaFamily = uaFamily;
    }

    public String getBrowserVersionInfo() {
        return browserVersionInfo;
    }

    public void setBrowserVersionInfo(String browserVersionInfo) {
        this.browserVersionInfo = browserVersionInfo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUaName() {
        return uaName;
    }

    public void setUaName(String uaName) {
        this.uaName = uaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
