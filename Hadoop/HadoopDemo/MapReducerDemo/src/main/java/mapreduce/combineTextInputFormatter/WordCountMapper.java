package mapreduce.combineTextInputFormatter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
    private Text outK = new Text();
    private IntWritable outV= new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String s = value.toString();
        String[] split = s.split("\t");
        for (String s1 : split) {
            outK.set(s1);
            context.write(outK,outV);
        }
        //        super.map(key, value, context);
    }
}
