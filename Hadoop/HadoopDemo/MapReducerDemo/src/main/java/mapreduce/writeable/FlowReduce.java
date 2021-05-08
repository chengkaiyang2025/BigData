package mapreduce.writeable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReduce extends Reducer<Text,FlowBean, Text, FlowBean> {
    private FlowBean outV = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long totalUp = 0;
        long totalDwon = 0;
        for (FlowBean value : values) {
            totalUp += value.getUpFlow();
            totalDwon += value.getDownFlow();
        }
        outV.setUpFlow(totalUp);
        outV.setDownFlow(totalDwon);
        outV.setSumFlow();
        context.write(key, outV);
    }
}
