package com.yzf.restartstrategy;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Random;

/**
 * 测试开窗状态下，检查点数据的保存情况。
 */
public class RestartStrategyStatefulTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart());
        env.setParallelism(1);
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        int windowmin = Integer.valueOf(parameterTool.get("windowmin","1"));
        int keysize = Integer.valueOf(parameterTool.get("keysize","1000"));
        int sleepmisec = Integer.valueOf(parameterTool.get("sleepmisec","1"));
        // 每30秒，如果失败次数达到4次，则失败，否则重启，
//        env.setRestartStrategy(RestartStrategies.failureRateRestart(
//                4,// 每个时间间隔的最大故障次数
//                Time.of(30, TimeUnit.SECONDS),// 测量故障率的时间间隔
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
                    long l2 = random.nextInt(keysize);
//                    System.out.println(l1);
//                    System.out.println(l2);
//                    if(count > 10){
//                        l2 = 0;
//                    }
                    ctx.collect(new Tuple3<>(l1,l2,count));
                    count += 1;
                    Thread.sleep(sleepmisec);

                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        SingleOutputStreamOperator<Tuple3<Long, Long, Integer>> sum = source.keyBy(1)
                .window(TumblingProcessingTimeWindows.of(Time.minutes(windowmin)))
                .sum(0);
        source.print("测试数据：");
        sum.print("结果：");
        env.execute(RestartStrategyStatefulTest.class.getName()+":flink的失败重启策略（有状态）");
    }

}
