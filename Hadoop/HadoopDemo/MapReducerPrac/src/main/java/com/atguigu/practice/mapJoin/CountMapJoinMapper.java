package com.atguigu.practice.mapJoin;

import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.util.CookieUtil;
import com.atguigu.practice.util.Parser;
import com.atguigu.practice.util.TokenInfoUtil;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CountMapJoinMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private Map<String,String> company = new HashMap<>();
    private Text outK = new Text();
    private NginxBean nginxBean = new NginxBean();
    private LongWritable outV = new LongWritable(1);
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();

        FileSystem fileSystem = FileSystem.get(context.getConfiguration());
        FSDataInputStream open = fileSystem.open(new Path(cacheFiles[0]));

        BufferedReader reader = new BufferedReader(new InputStreamReader(open, "UTF-8"));

        String line;
        while (StringUtils.isNotEmpty(line = reader.readLine())){
            String[] split = line.split(",");
            company.put(split[0], split[1]);
        }
        company.put("NULL","NULL");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String companyId = "NULL";
        try {
            Parser.jsonStringToPojo(value.toString(), nginxBean);
            String http_cookie = nginxBean.getHttp_cookie();

            if(!http_cookie.equals("NULL")){
                String access_token = CookieUtil.getCookieValueByCookieKey(http_cookie, "access_token");
                if(access_token != null && access_token.contains(".")){
                    companyId = TokenInfoUtil.decode(access_token).getGsId();
                }
            }
            outK.set(companyId+"-"+ Optional.ofNullable(company.get(companyId)).orElse("NULL"));
            context.write(outK, outV);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
