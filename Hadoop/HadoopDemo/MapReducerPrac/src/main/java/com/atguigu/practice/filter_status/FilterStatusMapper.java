package com.atguigu.practice.filter_status;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FilterStatusMapper extends Mapper<LongWritable, Text, Text, NginxBean> {
    private NginxBean nginxBean = new NginxBean();
    private Text outK = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String s = value.toString();
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        if(parse.getString("fields-set") == null){
            return;
        }

        nginxBean.setStatus(parse.getString("status"));
        nginxBean.setFields_set(parse.getString("fields-set"));
        nginxBean.setTime_local(parse.getString("time_local"));
        nginxBean.setRequest(parse.getString("request"));
        nginxBean.setRequest_length(parse.getLong("request_length"));
        nginxBean.setRequest_time(parse.getLong("request_time"));
        if(nginxBean.getStatus().equals("200")){
            outK.set(nginxBean.getFields_set());
            context.write(outK, nginxBean);
        }
    }
}
