package com.atguigu.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class WcDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、获取一个job实例
        Job job = Job.getInstance();

        // 2、设置类路径（classpath）
        job.setJarByClass(WcDriver.class);
        // 3、设置Mapper和Reducer
        job.setMapperClass(WcMapper.class);
        job.setReducerClass(WcReducer.class);

        // 4、设置Mapper和Reducer输出的类型

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 5、设置输入输出的数据

        FileInputFormat.setInputPaths(job, new Path("src/main/resources/wordcount.txt"));
        FileOutputFormat.setOutputPath(job, new Path("src/main/resources/result"));
        // 6、提交job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0:1);

    }
}
