package com.yzf.anticrawler.bean;

import org.apache.flink.types.Row;

import java.math.BigInteger;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：CSM 刘思远那边的反爬消息体
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
    public CSMRequestBody(Row row) {
        this.type = "按用户、接口维度统计访问频率";
//        this.desc = getDesc();
        String s = row.getField(0).toString();
        this.companyId = new BigInteger(String.valueOf(1111));
        this.createTime = row.getField(2).toString().replace("T"," ");
        if(this.createTime.length() == 16){
            this.createTime = this.createTime+":00";
        }
        this.desc = String.format("云帐房代帐系统帐号：%s，于%s点，极其频繁访问页面: %s，疑似存在爬虫行为",
                row.getField(1),
                this.createTime,
                row.getField(3));
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
