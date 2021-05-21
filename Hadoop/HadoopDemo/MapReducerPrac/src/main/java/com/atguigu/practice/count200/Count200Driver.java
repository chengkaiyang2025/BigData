package com.atguigu.practice.count200;


import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.outputformat.PlainFileOutputFomat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 *  2、select count(1) from nginx where status = 200
 */
public class Count200Driver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(Count200Driver.class);

        job.setMapperClass(Count200Mapper.class);
        job.setReducerClass(Count200Reducer.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(NginxBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(PlainFileOutputFomat.class);

        String input = "hdfs:///apps/data/warehouse/tmp/join/safe_interface_2021-05-14.json";
        String output = "hdfs:///apps/data/warehouse/tmp/output/filter_status";
        if(conf.get("fs.defaultFS").startsWith("file")){
            input = "MapReducerPrac/src/main/resources/join/safe_interface2.json";
            output = "MapReducerPrac/src/main/resources/output/filter_status";
        }

        job.setOutputFormatClass(PlainFileOutputFomat.class);
        // 如果在集群运行,写入一个文件一定要限制设置为1个reducer,否则其他reducer会获取不到权限
        job.setNumReduceTasks(1);
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        FileInputFormat.setMinInputSplitSize(job,2147483647);
        FileInputFormat.setMaxInputSplitSize(job,Long.MAX_VALUE);

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
