package com.atguigu.mapreduce.reduceJoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class TableMapper extends Mapper<LongWritable, Text, Text, TableBean> {

    private Text outK = new Text();
    private TableBean outV = new TableBean();
    private String fileName;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
//        super.setup(context);
        FileSplit split = (FileSplit) context.getInputSplit();

        fileName = split.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] split = value.toString().split("\t");
        if(fileName.contains("order")){
            outV.setId(split[0]);
            outV.setPid(split[1]);
            outV.setPname("");
            outV.setAmount(Integer.parseInt(split[2]));
            outV.setFlag("order");
            outK.set(split[1]);

        }else{
            outV.setId("");
            outV.setPid(split[0]);
            outV.setPname(split[1]);
            outV.setAmount(0);
            outV.setFlag("pd");
            outK.set(split[0]);
        }
        context.write(outK,outV);
    }
}
