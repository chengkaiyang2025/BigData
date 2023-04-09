package com.github.yck.simpletask;

import com.github.yck.datasource.DataGenerateStrategy;
import com.github.yck.datasource.WebLogDataGenerator;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 *
 */
public class StatelessTask {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> s = env.addSource(
                new WebLogDataGenerator(10, 100, DataGenerateStrategy.RANDOM_NUMBER)
                , "GenerateWebLogRandomly");
        s.map(new MapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                return "用户"+s.replace(WebLogDataGenerator.WEB_PREFIX,"")
                        .replace(WebLogDataGenerator.WEB_PREFIX,"")+"访问了:"+s;
            }
        }).name("find out which user access the link").print();
        env.execute("StatelessTask-将访问链接映射为字符串");
    }
}
