package com.yzf.source;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 处理时间语义的Flink源表
 */
public class RegisterProcessTimeFlinkTable {
    public static void registerSource(StreamExecutionEnvironment env, StreamTableEnvironment tableEnv){
        DataStream<Tuple4<String, Double, String, String>> source = env.addSource(new SourceFunction<Tuple4<String, Double,String,String>>() {
            final Random r = new Random();
            boolean isRunning = true;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            @Override
            public void run(SourceContext<Tuple4<String, Double,String,String>> sourceContext) throws Exception {
                while (isRunning){
                    Date d = new Date();
                    sourceContext.collect(new Tuple4<String, Double,String,String>("user" + r.nextInt(3),
                            r.nextDouble(),sdf.format(d),String.valueOf(d.getHours())))
                    ;
                    Thread.sleep(1000);
                }
            }


            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        Table table = tableEnv.fromDataStream(source,"f0 as user_id,f1 as order_amount,f2 as dt,f3 as hr,pc.proctime as proctime");
        tableEnv.createTemporaryView("source_table",table);
        table.printSchema();

    }
}
