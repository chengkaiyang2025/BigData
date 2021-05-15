package com.atguigu.practice.filter_status;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FilterStatusMapper extends Mapper<LongWritable, Text, NginxBean, NullWritable> {
    private NginxBean nginxBean;
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String s = value.toString();
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        nginxBean = parse.toJavaObject(NginxBean.class);
        if(nginxBean.getStatus().equals("200")){
            context.write(nginxBean, NullWritable.get() );
        }
    }
}
