package com.yzf.di.newdzjavalogtohive.stream;

import com.yzf.di.newdzjavalogtohive.bean.JavaLogBean;
import com.yzf.di.newdzjavalogtohive.sink.HiveSink;
import com.yzf.di.newdzjavalogtohive.source.KafkaSource;
import com.yzf.di.newdzjavalogtohive.util.DecodeJavaLogBeanUtil;
import com.yzf.di.newdzjavalogtohive.util.MyPropertiesUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

import static com.yzf.di.newdzjavalogtohive.constants.LogConstant.*;

/**
 * 上线
 * 本地Idea调试或者在线下调试时，加上参数 --mode dev
 *
 */
public class JavaLogToHiveStream {
    private static MyPropertiesUtil mp;
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        mp = new MyPropertiesUtil(parameterTool.get("mode", "prod"));
        System.setProperty("user.name",mp.get("sink.hive.user.name"));

        // 10分钟做一次checkpoint
        env.enableCheckpointing(1000*60*Integer.parseInt(mp.get("flink.checkpoint.interval.min")));
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);

        DataStream<JavaLogBean> kafkaSource = new KafkaSource().source(env, mp, "source.kafka.1.");

        DataStream<JavaLogBean> kafkaSource2 = new KafkaSource().source(env, mp,"source.kafka.2.");

        DataStream<JavaLogBean> union = kafkaSource.union(kafkaSource2);
//        kafkaSource.print();
        SingleOutputStreamOperator<JavaLogBean> map = union
                .filter(m ->  m.getMessage().contains(LOG_LEVEL_SEARCH_KEYWORD_ERROR) || m.getMessage().contains(LOG_LEVEL_SEARCH_KEYWORD_WARN))
                .map(DecodeJavaLogBeanUtil::decode)
                // 清洗LogBean中 的 message_logger_for_short 字段
                .map(DecodeJavaLogBeanUtil::messageLoggerForShort);
        new HiveSink().sink(env,map,mp);
//        env.execute();
    }
}
