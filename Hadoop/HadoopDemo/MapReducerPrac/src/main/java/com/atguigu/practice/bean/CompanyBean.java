package com.atguigu.practice.bean;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CompanyBean implements Writable {

    private String companyId;

    @Override
    public String toString() {
        return "CompanyBean{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String companyName;
    private String flag;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyBean() {
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.companyId);
        out.writeUTF(this.companyName);
        out.writeUTF(this.flag);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.companyId = in.readUTF();
        this.companyName = in.readUTF();
        this.flag = in.readUTF();
    }
}
