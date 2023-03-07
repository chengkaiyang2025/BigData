/*
 * Copyright (c) 2018. 南京云帐房网络科技有限公司 版权所有
 */
 
package com.yzf.di.newdzlogsinkhive.bean;


/**
 * @Description: accessToken中解析的信息
 * @Author: zhouxinghuai
 * @CreateDate: 2018/11/29 13:37
 * @UpdateDate: 2018/11/29 13:37
 * @Version: 1.0
 * @UpdateUser:
 * @UpdateRemark:
 */
public class TokenInfo {

    /**
     * 用户登录名，account
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGsId() {
        return gsId;
    }

    public void setGsId(String gsId) {
        this.gsId = gsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "用户信息" +
                "用户名：'" + userName + '\'' +
                ", 公司id：'" + gsId + '\'' +
                ", 用户id：" + userId + '\'' +
                ", 电话：" + phone + '\'' +
                ", 所属set：" + set + '\'' +
//                ", iss='" + iss + '\'' +
//                ", expiredTime='" + expiredTime + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     * 公司ID（代账用户公司ID）
     */
    private String gsId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * set
     */
    private String set;


    /**
     * 签发人
     */
    private String iss;

    /**
     * 过期日期
     */
    private String expiredTime;

}
