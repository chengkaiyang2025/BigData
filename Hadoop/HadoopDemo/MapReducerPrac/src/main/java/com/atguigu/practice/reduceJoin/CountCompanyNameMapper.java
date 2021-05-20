package com.atguigu.practice.reduceJoin;

import com.atguigu.practice.bean.CompanyBean;
import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.CookieUtil;
import com.atguigu.practice.util.Parser;
import com.atguigu.practice.util.TokenInfoUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class CountCompanyNameMapper extends Mapper<LongWritable, Text, Text, CompanyBean> {
    private String fileName;
    private Text outK = new Text();
    private NginxBean nginxBean = new NginxBean();
    private CompanyBean outV = new CompanyBean();
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit)context.getInputSplit();
        fileName = fileSplit.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String companyId = "NULL";
        String line = value.toString();
        if (fileName.endsWith(".json")){
            Parser.jsonStringToPojo(line, nginxBean);
            String http_cookie = nginxBean.getHttp_cookie();

            if(!http_cookie.equals("NULL")){
                String access_token = CookieUtil.getCookieValueByCookieKey(http_cookie, "access_token");
                if(access_token != null && access_token.contains(".")){
                    companyId = TokenInfoUtil.decode(access_token).getGsId();
                }
            }

            outV.setCompanyId(companyId);
            outV.setCompanyName("");
            outV.setFlag(".json");
            outK.set(companyId);

            context.write(outK, outV);
        }else if(fileName.endsWith(".csv")){
            String[] split = line.split(",");

            outV.setCompanyId(split[0]);
            outV.setCompanyName(split[1]);
            outV.setFlag(".csv");

            outK.set(split[0]);
            context.write(outK, outV);
        }
    }
}
