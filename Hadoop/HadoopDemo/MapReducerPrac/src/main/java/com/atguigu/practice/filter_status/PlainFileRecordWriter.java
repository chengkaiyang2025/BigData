package com.atguigu.practice.filter_status;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class PlainFileRecordWriter extends RecordWriter<NginxBean, NullWritable> {
    private FSDataOutputStream fs;

    public PlainFileRecordWriter(TaskAttemptContext job) {
        try{
            FileSystem fileSystem = FileSystem.get(job.getConfiguration());

            fs = fileSystem.create(new Path("Hadoop/HadoopDemo/MapReducerPrac/src/main/resources/output"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(NginxBean key, NullWritable value) throws IOException, InterruptedException {
        fs.writeBytes(key.toString()+"\n");
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        fs.close();
    }
}
