package com.atguigu.flink.beans;

import scala.Int;

public class SensorReading {
    private String name;
    private Long timeStamp;

    public SensorReading(String name, double tem) {
        this.name = name;
        this.tem = tem;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "name='" + name + '\'' +
                ", timeStamp=" + timeStamp +
                ", tem=" + tem +
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
