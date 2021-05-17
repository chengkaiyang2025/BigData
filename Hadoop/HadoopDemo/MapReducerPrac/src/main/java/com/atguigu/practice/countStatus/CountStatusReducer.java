package com.atguigu.practice.countStatus;

import com.atguigu.practice.bean.NginxBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigInteger;

public class CountStatusReducer extends Reducer<Text, NginxBean, Text, Text> {

    private Text outV = new Text();
    @Override
    protected void reduce(Text key, Iterable<NginxBean> values, Context context) throws IOException, InterruptedException {

        long sum = 0;
        for (NginxBean value : values) {
            sum += 1;
        }
        outV.set(key.toString()+",count:"+String.valueOf(sum));
        context.write(key, outV);
    }
}
