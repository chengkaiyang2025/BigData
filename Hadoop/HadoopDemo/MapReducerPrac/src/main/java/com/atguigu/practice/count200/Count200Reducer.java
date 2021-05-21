package com.atguigu.practice.count200;

import com.atguigu.practice.bean.NginxBean;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Count200Reducer extends Reducer<NullWritable, NginxBean, Text, Text> {
    private Text outV = new Text();
    private Text outK = new Text();
    @Override
    protected void reduce(NullWritable key, Iterable<NginxBean> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        for (NginxBean value : values) {
            count += 1;
        }
        outV.set(String.valueOf(count));
        outK.set("all");
        context.write(outK,outV);
    }
}
