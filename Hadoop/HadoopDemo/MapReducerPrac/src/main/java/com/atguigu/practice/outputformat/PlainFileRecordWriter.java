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

    public PlainFileRecordWriter(TaskAttemptContext job) {
        try{
            FileSystem fileSystem = FileSystem.get(job.getConfiguration());

            fs = fileSystem.create(new Path("MapReducerPrac/src/main/resources/result/out.txt"));
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
