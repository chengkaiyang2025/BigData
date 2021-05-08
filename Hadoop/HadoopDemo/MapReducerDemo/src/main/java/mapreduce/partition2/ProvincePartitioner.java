package mapreduce.partition2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class ProvincePartitioner extends Partitioner<Text,FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        String phone = text.toString();
        String subphone = phone.substring(0,3);

        if ("136".equals(subphone)){
            return 0;
        }else if("137".equals(subphone)){
            return 1;
        }else if("138".equals(subphone)){
            return 2;
        }else if("139".equals(subphone)){
            return 3;
        }else {
            return 4;
        }
    }
}
