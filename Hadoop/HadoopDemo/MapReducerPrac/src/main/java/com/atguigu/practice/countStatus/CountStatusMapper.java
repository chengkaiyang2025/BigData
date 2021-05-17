package com.atguigu.practice.countStatus;

import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.Parser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CountStatusMapper extends Mapper<LongWritable, Text, Text, NginxBean> {

    private Text outK = new Text();
    private NginxBean outV = new NginxBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Parser.jsonStringToPojo(value.toString(), outV);
        outK.set(outV.getStatus());
        context.write(outK, outV);
    }
}
