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

        String input = "MapReducerPrac/src/main/resources/safe_interface2.json";
        String output = "MapReducerPrac/src/main/resources/output/count2";

        if(args.length == 2){
            input = args[0];
            output = args[1];
        }
        job.setOutputFormatClass(PlainFileOutputFomat.class);
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
