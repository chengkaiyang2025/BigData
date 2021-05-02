package com.atguigu.mapreduce.partition2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、设置job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        // 2、设置jar
        job.setJarByClass(FlowDriver.class);
        // 3、设置mapper
        job.setMapperClass(FlowMapper.class);
        // 4、设置reducer
        job.setReducerClass(FlowReducer.class);
        // 5、设置mapper的output
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        // 6、设置output
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        // 7、设置输入
        FileInputFormat.setInputPaths(job,new Path("src/main/resources/input/11_input/inputflow/phone_data.txt"));
        // 8、设置输出
        FileOutputFormat.setOutputPath(job, new Path("src/main/resources/output/11_input/partition2"));
        // 设置partitoin
        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(6);
        // 9、提交工作流
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
