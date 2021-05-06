package com.atguigu.mapreduce.mapjoin;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MapJoinMapper extends Mapper<LongWritable,Text, Text, NullWritable> {
    private Map<String,String> pdMap = new HashMap<String,String>();
    private Text outK = new Text();
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        URI[] cacheFiles = context.getCacheFiles();

        FileSystem fileSystem = FileSystem.get(context.getConfiguration());
        FSDataInputStream fis = fileSystem.open(new Path(cacheFiles[0]));

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        String line;
        while(StringUtils.isNotEmpty(line = reader.readLine())){
            String[] split = line.split("\t");
            pdMap.put(split[0],split[1]);
        }
        IOUtils.closeStream(reader);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] split = value.toString().split("\t");
        String pName = pdMap.get(split[1]);
        outK.set(split[0]+"\t"+pName+"\t"+split[1]);
        context.write(outK,NullWritable.get());
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
