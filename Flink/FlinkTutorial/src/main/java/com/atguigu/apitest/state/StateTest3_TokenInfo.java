package com.atguigu.apitest.state;

import com.atguigu.apitest.beans.TokenInfo;
import com.atguigu.apitest.util.TokenInfoUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：普通的map模式，每来一条数据就直接map解析。
 * @date ：2021/3/9 下午6:33
 */


public class StateTest3_TokenInfo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        String file = "src/main/resources/nginx.log";
        DataStreamSource<String> source = env.readTextFile(file);
        SingleOutputStreamOperator<Tuple2<TokenInfo, String>> map = source.map(new MapFunction<String, Tuple2<TokenInfo, String>>() {
            @Override
            public Tuple2<TokenInfo, String> map(String value) throws Exception {
                String[] split = value.split(",");
                TokenInfo decode = TokenInfoUtil.decode(split[0]);
                return new Tuple2<>(decode, split[1]);
            }
        });
        map.print("");
        env.execute();

    }
}
