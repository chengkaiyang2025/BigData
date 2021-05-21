package com.atguigu.practice.mapJoin;

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
import java.net.URI;
import java.net.URISyntaxException;

public class CountMapJoinDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(CountMapJoinDriver.class);

        job.setMapperClass(CountMapJoinMapper.class);
        job.setReducerClass(CountMapJoinReducer.class);
        job.setCombinerClass(CountMapJoinCombiner.class);
        //  设置combiner
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setOutputFormatClass(PlainFileOutputFomat.class);

        String input = "hdfs:///apps/data/warehouse/tmp/join/safe_interface_2021-05-14.json";
        String output = "hdfs:///apps/data/warehouse/tmp/output/filter_status";
        String cacheFile = "hdfs:///apps/data/warehouse/tmp/join/companyName.csv";
        if(conf.get("fs.defaultFS").startsWith("file")){
            input = "MapReducerPrac/src/main/resources/join/safe_interface2.json";
            output = "MapReducerPrac/src/main/resources/output/filter_status";
            cacheFile = "/home/yzf/Bigdata/Hadoop/HadoopDemo/MapReducerPrac/src/main/resources/join/companyName.csv";
        }
        job.addCacheFile(new URI(cacheFile));
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
