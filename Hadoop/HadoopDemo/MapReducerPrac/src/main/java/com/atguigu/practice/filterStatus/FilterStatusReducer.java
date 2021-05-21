package com.atguigu.practice.filterStatus;

import com.atguigu.practice.bean.NginxBean;
import org.apache.hadoop.io.Text;
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
