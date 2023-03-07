package com.yzf.stateful;

import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用mapstate尽可能的减少decode方法的调用次数
 * 验证issue：
 * http://gitlab.yzf.net/group_di/bigdata/flink-job/issues/18
 */
public class MapStatefulTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        List<Tuple3<String,String,String>> log = new ArrayList<>();
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie12","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie13","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie12","http://www.baidu.com/3/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie3","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie12","http://www.baidu.com/3"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie3","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com/1"));

        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com"));
        log.add(new Tuple3<String,String,String>("nginx2","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx2","cookie12","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie13","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie12","http://www.baidu.com/3/1"));
        log.add(new Tuple3<String,String,String>("nginx2","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx2","cookie3","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie12","http://www.baidu.com/3"));
        log.add(new Tuple3<String,String,String>("nginx2","cookie1","http://www.baidu.com/1"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie3","http://www.baidu.com/2"));
        log.add(new Tuple3<String,String,String>("nginx1","cookie1","http://www.baidu.com/1"));
        env.setParallelism(1);
        DataStreamSource<Tuple3<String,String,String>> source = env.addSource(new SourceFunction<Tuple3<String,String,String>>() {
            boolean isRunning = true;
            @Override
            public void run(SourceContext<Tuple3<String,String,String>> ctx) throws Exception {
                while (isRunning){
                    for (Tuple3<String,String,String> t : log) {
                        ctx.collect(t);
                        Thread.sleep(100);
                    }
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        SingleOutputStreamOperator<Tuple3<String, String, String>> process = source.keyBy(0).process(new KeyedProcessFunction<Tuple, Tuple3<String, String, String>, Tuple3<String, String, String>>() {
            private MapState<String,String> cookieMapState;

            @Override
            public void open(Configuration parameters) throws Exception {
                StateTtlConfig cookieStateTtlConfig1 = StateTtlConfig.newBuilder(Time.minutes(5))
                        .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                        .setStateVisibility(StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp)
                        .build();
                MapStateDescriptor<String, String> cookieStateDescriptor = new MapStateDescriptor<>(
                        "cookie", String.class, String.class
                );
                cookieStateDescriptor.enableTimeToLive(cookieStateTtlConfig1);
                cookieMapState = getRuntimeContext().getMapState(cookieStateDescriptor);

            }

            @Override
            public void close() throws Exception {
                // 如果使用rocksdb，这里clear会导致tm挂掉，其他时候调用clear方法不会有错。
                cookieMapState.clear();

            }

            @Override
            public void processElement(Tuple3<String, String, String> value, Context ctx, Collector<Tuple3<String, String, String>> out) throws Exception {
                String userId;
                if(cookieMapState.contains(value.f1)){
                    userId = cookieMapState.get(value.f1);
                    System.out.println(value.f1+"命中");
                }else{
                    userId = decodeCookie(value.f1);
                    cookieMapState.put(value.f1,userId);
                    System.out.println(value.f1+"未命中");

                }
                Tuple3<String, String, String> re = new Tuple3<>(value.f0, decodeCookie(value.f1), value.f2);
                out.collect(re);
            }
        });
        process.print("用户访问");
        env.execute("测试在rocksdb的后端存储中，mapstate调用clear方法导致TaskManager宕机");
    }
    public static String decodeCookie(String cookie){
        return cookie.replace("cookie","user");
    }
}
