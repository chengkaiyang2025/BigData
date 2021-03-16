package com.atguigu.apitest.beans;

import java.math.BigInteger;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/15 下午6:59
 */


public class SensorReadingSimple {
    private String name;
    private BigInteger temp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getTemp() {
        return temp;
    }

    public void setTemp(BigInteger temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "SensorReadingSimple{" +
                "name='" + name + '\'' +
                ", temp='" + temp + '\'' +
                '}';
    }

    public SensorReadingSimple() {
    }

    public SensorReadingSimple(String name, BigInteger temp) {
        this.name = name;
        this.temp = temp;
    }
}
