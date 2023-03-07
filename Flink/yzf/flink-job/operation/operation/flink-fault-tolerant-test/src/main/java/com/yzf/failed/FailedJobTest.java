package com.yzf.failed;


import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 无重启策略、运行一段时间后抛出异常，用于flink job健康检查用
 */
public class FailedJobTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart());
        env.setParallelism(1);
        // 每30秒，如果失败次数达到4次，则失败，否则重启，
//        env.setRestartStrategy(RestartStrategies.failureRateRestart(
//                4,// 每个时间间隔的最大故障次数
//                org.apache.flink.api.common.time.Time.of(30, TimeUnit.SECONDS),// 测量故障率的时间间隔
//                Time.of(5, TimeUnit.SECONDS)// 延时
//        ));
        /**
         *
         */
        DataStreamSource<Tuple3<Long, Long,Integer>> source = env.addSource(new SourceFunction<Tuple3<Long, Long,Integer>>() {
            boolean isRunning = true;
            Random random = new Random();
            @Override
            public void run(SourceContext<Tuple3<Long, Long,Integer>> ctx) throws Exception {
                int count = 0;
                while (true){
                    int partitionKey = random.nextInt(5);
                    long l1 = random.nextInt(10000);
                    long l2 = random.nextInt(100);
//                    System.out.println(l1);
//                    System.out.println(l2);
                    if(count > 10){
                        l2 = 0;
                    }
                    ctx.collect(new Tuple3<>(l1,l2,count));
                    count += 1;
                    Thread.sleep(100);

                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<Long> map = source.map(new MapFunction<Tuple3<Long, Long, Integer>, Long>() {
            @Override
            public Long map(Tuple3<Long, Long, Integer> t) throws Exception {
                return t.f0 / t.f1;
            }
        });
        source.print("测试数据：");
        map.print("结果：");
        env.execute(FailedJobTest.class.getName()+":用于flink job健康检查用");
    }

}
