package com.atguigu.practice.reduceJoin;


import com.atguigu.practice.bean.CompanyBean;
import com.atguigu.practice.outputformat.PlainFileOutputFomat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class CountCompanyNameDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(CountCompanyNameDriver.class);

        job.setMapperClass(CountCompanyNameMapper.class);
        job.setReducerClass(CountCompanyNameReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CompanyBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setOutputFormatClass(PlainFileOutputFomat.class);

        String input = "hdfs:///apps/data/warehouse/tmp/join";
        String output = "hdfs:///apps/data/warehouse/tmp/output/filter_status";
        if(conf.get("fs.defaultFS").startsWith("file")){
            input = "MapReducerPrac/src/main/resources/join";
            output = "MapReducerPrac/src/main/resources/output/filter_status";
        }

        job.setOutputFormatClass(PlainFileOutputFomat.class);
        job.setNumReduceTasks(1);

        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        FileInputFormat.setMinInputSplitSize(job,2147483647);
        FileInputFormat.setMaxInputSplitSize(job,Long.MAX_VALUE);
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
