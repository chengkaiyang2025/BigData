package com.yzf.di.kuducdc.kudutest.source;

import com.yzf.di.kuducdc.kudutest.util.OctopusDataGenerator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成不同的帐期的数据源
 */
public class OctopusAutoGenerateSource {
    private FileInputStream fstream = null;

    /** 压力测试用，快速产生kjnd，kjqj数据。
     * @param env
     * @return
     * @throws IOException
     */
    public DataStream<ObjectNode> randomPressureSource(StreamExecutionEnvironment env) throws IOException {

        DataStreamSource<ObjectNode> source = env.addSource(new SourceFunction<ObjectNode>() {
            boolean isRunning = true;
            @Override
            public void run(SourceContext<ObjectNode> ctx) throws Exception {
                while(isRunning){
                    ObjectNode o = OctopusDataGenerator.randomGenerateOctopusData();
                    ctx.collect(o);
                }

            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        return source;
    }

    /**
     * @param env 生成不同kjndkjqj的数据
     * @return
     * @throws IOException
     */
    public DataStream<ObjectNode> differentKjndKJqjSource(StreamExecutionEnvironment env) throws IOException {
        fstream = new FileInputStream("/data/yzf/IdeaWorkSpace/flink-job/kudu-cdc/kudutest/src/main/resources/different_kjnd_kjqj_source_data.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        List<String> lines = new ArrayList<>();
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            lines.add(strLine);
        }
        fstream.close();
        DataStreamSource<ObjectNode> source = env.addSource(new SourceFunction<ObjectNode>() {
            boolean isRunning = true;
            @Override
            public void run(SourceContext<ObjectNode> ctx) throws Exception {
                while(isRunning){
                    for (String line : lines) {
                        JsonNode jsonNode = new ObjectMapper().readTree(line);
                        ctx.collect(jsonNode.deepCopy());
                        Thread.sleep(1000);
                    }
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
