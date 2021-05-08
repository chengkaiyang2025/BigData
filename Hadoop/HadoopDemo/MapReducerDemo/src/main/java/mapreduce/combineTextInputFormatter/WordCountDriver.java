package mapreduce.combineTextInputFormatter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1、获取job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2、设置jar
        job.setJarByClass(WordCountDriver.class);
        // 3、设置mapper
        job.setMapperClass(WordCountMapper.class);
        // 4、设置reducer
        job.setReducerClass(WordCountReducer.class);
        // 5、设置mapper output
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        // 6、设置reducer output
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setInputFormatClass(CombineTextInputFormat.class);
        CombineTextInputFormat.setMaxInputSplitSize(job, 20971520*2);

        // 7、设置in path
        CombineTextInputFormat.setInputPaths(job,new Path("MapReducerDemo/src/main/resources/11_input/inputcombinetextinputformat"));
        FileOutputFormat.setOutputPath(job,new Path("MapReducerDemo/src/main/resources/output/11_input/inputcombinetextinputformat"));


        // 8
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
