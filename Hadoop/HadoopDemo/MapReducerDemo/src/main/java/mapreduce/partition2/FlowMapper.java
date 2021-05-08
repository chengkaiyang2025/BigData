package mapreduce.partition2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text,Text,FlowBean> {
    private Text outK = new Text();
    private FlowBean outV = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String[] split = value.toString().split("\t");

        outK.set(split[1]);

        outV.setUpFlow(Long.parseLong(split[split.length - 3]));
        outV.setDownFlow(Long.parseLong(split[split.length - 2]));
        outV.setSumFlow();

        context.write(outK, outV);
    }
}
