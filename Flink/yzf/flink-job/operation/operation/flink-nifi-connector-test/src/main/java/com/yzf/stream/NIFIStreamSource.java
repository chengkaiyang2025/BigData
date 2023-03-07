package com.yzf.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.nifi.NiFiDataPacket;
import org.apache.flink.streaming.connectors.nifi.NiFiSource;
import org.apache.nifi.remote.client.SiteToSiteClient;
import org.apache.nifi.remote.client.SiteToSiteClientConfig;

public class NIFIStreamSource {
    public static void main(String[] args) throws Exception {



        StreamExecutionEnvironment streamExecEnv = StreamExecutionEnvironment.getExecutionEnvironment();

        SiteToSiteClientConfig clientConfig = new SiteToSiteClient.Builder()
                .url("http://172.24.10.5:18080/nifi")
                .portName("DataForFlink1")
                .requestBatchCount(1)
                .buildConfig();

        SourceFunction<NiFiDataPacket> nifiSource = new NiFiSource(clientConfig);
        SingleOutputStreamOperator<String> map = streamExecEnv.addSource(nifiSource).map(new MapFunction<NiFiDataPacket, String>() {
            @Override
            public String map(NiFiDataPacket niFiDataPacket) throws Exception {

                String s = new String(niFiDataPacket.getContent());

                return s;
            }
        });
        map.print();
        streamExecEnv.execute("nifi 测试");
    }
}
