package com.yzf.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.nifi.NiFiDataPacket;
import org.apache.flink.streaming.connectors.nifi.NiFiDataPacketBuilder;
import org.apache.flink.streaming.connectors.nifi.NiFiSink;
import org.apache.flink.streaming.connectors.nifi.NiFiSource;
import org.apache.nifi.remote.client.SiteToSiteClient;
import org.apache.nifi.remote.client.SiteToSiteClientConfig;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NIFIStreamSink {
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

                String s = new String(niFiDataPacket.getContent())+"ETL BY Flink";

                return s;
            }
        });

        SingleOutputStreamOperator<NiFiDataPacket> map1 = map.map(new MapFunction<String, NiFiDataPacket>() {
            @Override
            public NiFiDataPacket map(String s) throws Exception {
                return new NiFiDataPacket(){
                    @Override
                    public byte[] getContent() {
                        return s.getBytes(StandardCharsets.UTF_8);
                    }

                    @Override
                    public Map<String, String> getAttributes() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("uuid", UUID.randomUUID().toString().replaceAll("-",""));
                        return map;
                    }
                };

            }
        });

        SiteToSiteClientConfig clientConfig2 = new SiteToSiteClient.Builder()
                .url("http://172.24.10.5:18080/nifi")
                .portName("DataForFlink2")
                .requestBatchCount(1)
                .buildConfig();

        NiFiSink<NiFiDataPacket> niFiDataPacketNiFiSink = new NiFiSink<>(clientConfig2, new NiFiDataPacketBuilder<NiFiDataPacket>() {
            @Override
            public NiFiDataPacket createNiFiDataPacket(NiFiDataPacket niFiDataPacket, RuntimeContext runtimeContext) {
                return niFiDataPacket;
            }
        });
        map1.addSink(niFiDataPacketNiFiSink);
        map.print();
        streamExecEnv.execute("nifi 测试");
    }
}
