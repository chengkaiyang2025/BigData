package com.atguigu.practice.filter_status;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.Parser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FilterStatusMapper extends Mapper<LongWritable, Text, Text, NginxBean> {
    private NginxBean nginxBean = new NginxBean();
    private Text outK = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        Parser.jsonStringToPojo(value.toString(), nginxBean);
        try {
            if(nginxBean.getStatus() != null && nginxBean.getStatus().equals("403")){
                outK.set(nginxBean.getFields_set());
                context.write(outK, nginxBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
