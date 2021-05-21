package com.atguigu.practice.countCompany;

import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.CookieUtil;
import com.atguigu.practice.util.Parser;
import com.atguigu.practice.util.TokenInfoUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Optional;

public class CountCompanyMapper extends Mapper<LongWritable, Text, Text ,LongWritable> {
    private NginxBean nginxBean = new NginxBean();
    private LongWritable l = new LongWritable(1);
    private Text outK = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            Parser.jsonStringToPojo(value.toString(),nginxBean);
            String http_cookie = nginxBean.getHttp_cookie();
            String gsid = "NULL";
            if(!http_cookie.equals("NULL")){
                String access_token = CookieUtil.getCookieValueByCookieKey(http_cookie, "access_token");
                if (access_token != null && access_token.contains(".")){
                    gsid = TokenInfoUtil.decode(access_token).getGsId();
                }
            }
            outK.set(gsid);
            context.write(outK, l);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
