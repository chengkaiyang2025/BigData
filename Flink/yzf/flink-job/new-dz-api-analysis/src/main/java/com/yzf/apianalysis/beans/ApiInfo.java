package com.yzf.apianalysis.beans;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/19 16:33
 * @description：
 */
public class ApiInfo {
    private String id;
    private Integer number;
    private Long timestamp;

    public ApiInfo() {
    }

    public ApiInfo(String id, Integer number, Long timestamp) {
        this.id = id;
        this.number = number;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" + "\"id\":\"" + id + "\",\"offset\":" + number + ",\"fetch_time\":" + timestamp + "}";
    }
}
