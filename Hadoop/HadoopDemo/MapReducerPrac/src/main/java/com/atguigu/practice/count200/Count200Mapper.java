package com.atguigu.practice.count200;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.Parser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import javax.swing.text.TabExpander;
import java.io.IOException;

public class Count200Mapper extends Mapper<LongWritable, Text, NullWritable, NginxBean> {
    private NginxBean outV = new NginxBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Parser.jsonStringToPojo(value.toString(), outV);
        if(outV.getStatus() != null && "200".equals(outV.getStatus())){
            context.write(NullWritable.get(), outV);
        }
    }
}
