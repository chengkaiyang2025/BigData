package com.atguigu.practice.filter_status;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FilterStatusReducer extends Reducer<Text, NginxBean, Text, Text> {

    private Text t = new Text();
    @Override
    protected void reduce(Text key, Iterable<NginxBean> values, Context context) throws IOException, InterruptedException {
        for (NginxBean value : values) {
            t.set(value.toString());
            context.write(key, t);
        }
    }
}
