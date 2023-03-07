package com.yzf.di.bean;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/18 11:30
 * @description：
 */
public class TableChanges {
    private String type;
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TableChanges{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
