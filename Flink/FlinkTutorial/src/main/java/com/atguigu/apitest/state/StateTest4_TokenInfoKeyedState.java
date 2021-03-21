package com.atguigu.apitest.state;

import com.atguigu.apitest.beans.TokenInfo;
import com.atguigu.apitest.util.TokenInfoUtil;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：使用状态后端保存ua信息，不用每次都。
 * @date ：2021/3/9 下午6:33
 */


public class StateTest4_TokenInfoKeyedState {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        String file = "src/main/resources/nginx.log";
        DataStreamSource<String> source = env.readTextFile(file);

        SingleOutputStreamOperator<Tuple2<TokenInfo, String>> map = source.keyBy(new KeySelector<String, String>() {
            @Override
            public String getKey(String value) throws Exception {
                return value.split(",")[0];
            }
        }).map(new TokenInfoMapper());
        map.print("使用状态后端保存常用token");
        env.execute();
    }

    public static class TokenInfoMapper extends RichMapFunction<String, Tuple2<TokenInfo,String>>{
        // 定义mapstate，记录常用token对应的tokeninfo pojo类。
        private MapState<String,TokenInfo> mapState;
        @Override
        public void open(Configuration parameters) throws Exception {
            mapState = getRuntimeContext().getMapState(
                    new MapStateDescriptor<String, TokenInfo>("token-mapper",String.class,TokenInfo.class)
            );
//            super.open(parameters);
        }

        @Override
        public void close() throws Exception {
            mapState.clear();
            super.close();
        }

        @Override
        public Tuple2<TokenInfo,String> map(String value) throws Exception {

            String[] split = value.split(",");
            String token = split[0];
            TokenInfo decode;
            // 如果mapState中存在token则直接取，如果没有则放入
            if(mapState.contains(token)){
                decode = mapState.get(token);
            }else {
                decode = TokenInfoUtil.decode(token);
                mapState.put(token,decode);
            }
            // TODO 如果mapState中token过多则清除


            return new Tuple2<>(decode,split[1]);
        }
    }
}
