package com.atguigu.practice.filterStatus;

import com.atguigu.practice.bean.NginxBean;
import com.atguigu.practice.outputformat.PlainFileOutputFomat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * https://github.com/yunzhangfang-di/Bigdata/issues/2
 *  1、select * from nginx where status = 404
 */
public class FilterStatusDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(FilterStatusDriver.class);
        job.setMapperClass(FilterStatusMapper.class);
        job.setReducerClass(FilterStatusReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NginxBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        String input = "hdfs:///apps/data/warehouse/tmp/join/safe_interface_2021-05-14.json";
        String output = "hdfs:///apps/data/warehouse/tmp/output/filter_status";
        if(conf.get("fs.defaultFS").startsWith("file")){
            input = "MapReducerPrac/src/main/resources/join/safe_interface2.json";
            output = "MapReducerPrac/src/main/resources/output/filter_status";
        }


        job.setOutputFormatClass(PlainFileOutputFomat.class);
        // 如果在集群运行,写入一个文件一定要限制设置为1个reducer,否则其他reducer会获取不到权限
        job.setNumReduceTasks(1);
        // 设置分片最小为
//        FileInputFormat.setMinInputSplitSize(job,1024*1024*1024*4);
//        FileInputFormat.setMaxInputSplitSize(job,1024*1024*1024*10);
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
//        job.setInputFormatClass(FileInputFormat.class);
        // 必须 setInputPaths之后，必须 小于long 2147483647,big than this number will cause stack when running in hadoop cluster.
        FileInputFormat.setMinInputSplitSize(job,2147483647);
        FileInputFormat.setMaxInputSplitSize(job,Long.MAX_VALUE);

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
