package com.atguigu.mapreduce.writeableComparable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
    private FlowBean outK = new FlowBean();
    private Text outV = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] split = value.toString().split("\t");

        String phone = split[1];

        long upFlow = Long.parseLong(split[split.length - 3]);
        long downFlow = Long.parseLong(split[split.length - 2]);

        outV.set(phone);
        outK.setUpFlow(upFlow);
        outK.setDownFlow(downFlow);
        outK.setSumFLow();

        context.write(outK, outV);
    }
}
