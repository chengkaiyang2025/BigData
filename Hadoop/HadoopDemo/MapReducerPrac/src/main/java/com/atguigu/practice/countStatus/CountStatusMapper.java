package com.atguigu.practice.countStatus;

import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.Parser;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CountStatusMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    private Text outK = new Text();
    private LongWritable outV = new LongWritable();
    private NginxBean nginxBean = new NginxBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Parser.jsonStringToPojo(value.toString(), nginxBean);
        outK.set(nginxBean.getStatus());
        outV.set(1);
        context.write(outK, outV);
    }
}
