package com.yzf.di.bean;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/8/13 15:59
 * @description：
 */
public class TokenInfo {

    private String userName;
    private String gsId;
    private String userId;
    private String phone;
    private String set;

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

    @Override
    public String toString() {
        return "TokenInfo{" +
                "userName='" + userName + '\'' +
                ", gsId='" + gsId + '\'' +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", set='" + set + '\'' +
                '}';
    }
}
