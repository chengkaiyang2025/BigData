package com.atguigu.mapreduce.partition2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text,FlowBean, Text,FlowBean> {
    private FlowBean outV = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
//        super.reduce(key, values, context);
        long upFLow = 0;
        long downFLow = 0;
//        long sumFLow = 0;
        for (FlowBean value : values) {
            upFLow += value.getUpFlow();
            downFLow += value.getDownFlow();
        }
        outV.setUpFlow(upFLow);
        outV.setDownFlow(downFLow);
        outV.setSumFlow();
        context.write(key, outV);
    }
}
