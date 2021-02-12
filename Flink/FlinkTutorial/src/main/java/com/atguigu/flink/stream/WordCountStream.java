package com.atguigu.flink.stream;

import com.atguigu.flink.batch.WordCount;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;


import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCountStream {
    public static void main(String[] args) throws Exception{
        // 创建流式处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // 设置并行度，默认值=当前计算机的CPU逻辑核心数（设置成1就是单线程）
//        env.setMaxParallelism(32);
        
        // 从文本中读取数据
        String inputPath = "src/main/resources/hello.txt";
        DataStream<String> inputDataStream = env.readTextFile(inputPath);
        
        // 基于流进行转换计算
        DataStream<Tuple2<String,Integer>> resultStream =
                inputDataStream.flatMap(new WordCount.MyFlatMapper())
                        .keyBy(item->item.f0).sum(1);

        resultStream.print();

        // 执行任务
        env.execute();
    }

}
