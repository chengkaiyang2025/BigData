package com.atguigu.practice.reduceJoin;

import com.atguigu.practice.bean.CompanyBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class CountCompanyNameReducer extends Reducer<Text, CompanyBean, Text, Text> {
    private Text outK = new Text();
    private Text outV = new Text();

    @Override
    protected void reduce(Text key, Iterable<CompanyBean> values, Context context) throws IOException, InterruptedException {
        String companyName = "NULL";

        String companyId = key.toString();
        long sum = 0;
        for (CompanyBean value : values) {
            if(value.getFlag().equals(".json")){
                sum += 1;
            }else if(value.getFlag().equals(".csv")){
                companyName = value.getCompanyName();
            }
        }
        outK.set(companyId+"-"+companyName);
        outV.set(companyId+"-"+companyName+":"+String.valueOf(sum));
        context.write(outK, outV);
    }
}
