package com.atguigu.practice.countCompany;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CountCompanyReducer extends Reducer<Text, LongWritable, Text, Text> {
    private Text outV = new Text();
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable value : values) {
            sum += value.get();
        }
        outV.set(key.toString()+",count:"+sum);
        context.write(key, outV);
    }
}
