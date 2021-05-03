package com.atguigu.mapreduce.writeableComparable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements WritableComparable<FlowBean> {

    private long upFlow;
    private long downFlow;
    private long sumFLow;

    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFLow() {
        return sumFLow;
    }

    public void setSumFLow() {
        this.sumFLow = this.upFlow + this.downFlow;
    }

    public FlowBean() {
    }

    @Override
    public int compareTo(FlowBean o) {
        if(this.sumFLow > o.getSumFLow()){
            return -1;
        }else if (this.sumFLow<o.getSumFLow()){
            return 1;
        }else {
            if(this.upFlow > o.upFlow){
                return 1;
            }else if(this.upFlow < o.upFlow){
                return -1;
            }else {
                return 0;
            }
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(this.upFlow);
        out.writeLong(this.downFlow);
        out.writeLong(this.sumFLow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readLong();
        this.downFlow = in.readLong();
        this.sumFLow = in.readLong();
    }
}
