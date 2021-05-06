package com.atguigu.mapreduce.writeable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable,Text, Text, FlowBean> {
    private Text outK = new Text();
    private FlowBean outV = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String s = value.toString();
        // 1	13736230513	192.196.100.1	www.atguigu.com	2481	2468
        String[] split = s.split("\t");

        // 手机号
        String phone = split[1];
        // 上行流量
        String up = split[split.length - 3];
        // 下行流量
        String down = split[split.length - 2];

        outV.setUpFlow(Long.parseLong(up));
        outV.setDownFlow(Long.parseLong(down));
        outV.setSumFlow();

        outK.set(phone);

//        super.map(key, value, context);
        context.write(outK,outV);
    }
}
