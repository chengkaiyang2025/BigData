package com.atguigu.practice.outputformat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class PlainFileRecordWriter extends RecordWriter<Text, Text> {
    private FSDataOutputStream fs;
    // local
    private static final String localOutputPath = "MapReducerPrac/src/main/resources/result/out.txt";
    private static final String remoteOutputPath = "hdfs://master.prd.yzf:8020/apps/data/warehouse/tmp/out.txt";

    public PlainFileRecordWriter(TaskAttemptContext job) {
        try{
            FileSystem fileSystem = FileSystem.get(job.getConfiguration());
            if(job.getConfiguration().get("fs.defaultFS").startsWith("file:///")){
                fs = fileSystem.create(new Path(localOutputPath));
            }else {
                fs = fileSystem.create(new Path(remoteOutputPath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Text key, Text value) throws IOException, InterruptedException {
        fs.writeBytes(value.toString()+"\n");
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        fs.close();
    }
}
