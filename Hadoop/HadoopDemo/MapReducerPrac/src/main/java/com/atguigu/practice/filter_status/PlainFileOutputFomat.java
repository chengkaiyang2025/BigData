package com.atguigu.practice.filter_status;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Progressable;

import java.io.IOException;

public class PlainFileOutputFomat extends FileOutputFormat<Text, Text> {
    @Override
    public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        PlainFileRecordWriter pfw = new PlainFileRecordWriter(job);
        return pfw;

    }
}
