package com.atguigu.apitest.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorReading {
    private String name;
    private Long timeStamp;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    public SensorReading(String name, double tem) {
        this.name = name;
        this.tem = tem;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "传感器名称='" + name + '\'' +
//                ", 时间=" + sdf.format(new Date(this.timeStamp)) +
                ", timeStamp=" + timeStamp +
                ", 温度=" + tem +
                '}';
    }

    public SensorReading() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getTem() {
        return tem;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    public SensorReading(String name, Long timeStamp, double tem) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.tem = tem;
    }

    private double tem;
}
