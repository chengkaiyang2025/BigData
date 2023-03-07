package com.yzf.di.kuducdc.kudutest.source;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * 测试用，随机生成kudu数据
 */
public class DemoAutoGenerateSource {
    public DataStreamSource<Tuple4<String, String, String, String>> source(StreamExecutionEnvironment env){
        DataStreamSource<Tuple4<String, String, String, String>> source = env.addSource(new SourceFunction<Tuple4<String, String, String, String>>() {
            boolean isRunning = true;
            Random r = new Random();
            @Override
            public void run(SourceContext<Tuple4<String, String, String, String>> ctx) throws Exception {
                while(isRunning){

//                    Thread.sleep(1);
                    ctx.collect(
                            new Tuple4<String, String, String, String>(
                                    String.valueOf(r.nextLong()),
                                    String.valueOf(r.nextInt(256)),
                                    String.valueOf("Nchange"),
                                    String.valueOf(r.nextLong())
                            )
                    );
                }

            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        return source;

    }
}
