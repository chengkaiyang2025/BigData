package com.yzf.di.newdzlogsinkhive.stream;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.sink.HiveSink;
import com.yzf.di.newdzlogsinkhive.source.KafkaSource;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Random;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：TODO
 * @date ：2021/3/11 下午2:38
 * 按照flink官方文档建议，这里为每一个算子添加uid，便于标识。
 * https://ci.apache.org/projects/flink/flink-docs-master/zh/docs/ops/state/savepoints/#%e5%88%86%e9%85%8d%e7%ae%97%e5%ad%90-id
 */


public class NginxAnalysisStream {


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        // TODO 线下测试！
        String bootstrapSource = parameterTool.get("bootstrapSource","172.16.190.35:9092,172.16.190.36:9092,172.16.190.37:9092");
//        String bootstrapSource = parameterTool.get("bootstrapSource","172.24.10.2:9092,172.24.10.3:9092,172.24.10.4:9092,172.24.10.5:9092");
        String hiveConfDir = parameterTool.get("hive_conf_dir","/data/flink/emr_hive_conf/conf");
        // TODO 线下测试！
        int parallelism = 4;
        // 2小时做一次checkpoint
        env.enableCheckpointing(1000*60*60*2);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        DataStream<LogBean> kafkaSource = new KafkaSource().source(env,bootstrapSource,parallelism);

        SingleOutputStreamOperator<LogBean> parseCookieAndUA = kafkaSource.filter(new FilterFunction<LogBean>() {
            /**
             * @param value
             * @return 过滤请求不为空而且 (host-name或beat-name是新代帐的）
             * @throws Exception
             */
            @Override
            public boolean filter(LogBean value) throws Exception {
//                Thread.sleep(100);

                return value.getRequest() != null ;
                // 空指针
//                        &&
//                        ( value.getHost_name().startsWith("fintax-web")||value.getBeat_name().startsWith("fintax-web"));
            }
        }).setParallelism(parallelism)
                .keyBy(new KeySelector<LogBean, Integer>() {
           private final Random r = new Random();

                    @Override
            public Integer getKey(LogBean logBean) throws Exception {
                try{
                    // 新代帐按照用户来源的外网ip进行分组，而一个cookie基本来源于一个外网ip
                    // 这里按照cookie的长度取mod，避免数据倾斜，同时保证key的个数为parallelism个，而状态后端的变量也为parallelism个
                    return logBean.getHttp_cookie().length() % parallelism;

                }catch (Exception e){
                    return r.nextInt(parallelism);
                }
            }
        }).process(new DecodeCookieMapper()).setParallelism(parallelism)
                ;
//        parseCookieAndUA.print();
        new HiveSink().sink(env,parseCookieAndUA,hiveConfDir);
        // 上线时候去掉这一行
//        env.execute("nginx实时数据分析-1、解析cookie与ua信息-process 全部业务逻辑 多并行度 解决key问题");
    }
}
