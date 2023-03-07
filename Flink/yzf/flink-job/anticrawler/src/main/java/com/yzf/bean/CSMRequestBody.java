package com.yzf.bean;

import java.math.BigInteger;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/2/3 下午2:06
 */


public class CSMRequestBody {
    private static final String projectName = "anti-crawler-kickass";
    private String type;
    private static final Integer level = 0;
    private String desc;
    private BigInteger companyId;
    private static final String companyName ="";
    private String createTime;
    public CSMRequestBody(String type, String desc, BigInteger companyId, String createTime) {
        this.type = type;
        this.desc = desc;
        this.companyId = companyId;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CSM{" +
                "projectName='" + projectName + '\'' +
                ", type='" + type + '\'' +
                ", level=" + level +
                ", desc='" + desc + '\'' +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public String getProjectName() {
        return projectName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigInteger getCompanyId() {
        return companyId;
    }

    public void setCompanyId(BigInteger companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


}
