package com.atguigu.mapreduce.writeableComparable;

import javafx.scene.text.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、创建job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        // 2、设置class
        job.setJarByClass(FlowDriver.class);
        // 3、设置mapper、与reducer
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);
        // 4、设置mapper 的output k/v
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);
        // 5、设置reducer 的output k/v
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        // 6、设置input 与 output的路径
        FileInputFormat.setInputPaths(job, new Path("src/main/resources/input/11_input/inputflow/phone_data.txt"));
        FileOutputFormat.setOutputPath(job, new Path("src/main/resources/output/11_input/writeableComparable"));
        // 7、提交job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
