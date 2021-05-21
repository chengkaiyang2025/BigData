package com.atguigu.practice.mapJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class CountMapJoinReducer extends Reducer<Text,LongWritable, Text ,Text> {
    private Text outV = new Text();
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable value : values) {
            sum += value.get();
        }
        outV.set(key.toString()+":"+String.valueOf(sum));
        context.write(key, outV);
    }
}
