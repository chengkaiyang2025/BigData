package com.atguigu.apitest.sink;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从文件中读取日志，写入到kafka中，模拟用户点击流。
 * @date ：2021/3/16 下午2:41
 */


public class KafkaSink {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> source = env.addSource(new SourceFunction<String>() {

            boolean isRunning = true;

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                List<String> lines = new ArrayList<String>();
                String file = "src/main/resources/output.csv";
                InputStream ins = null; // raw byte-stream
                Reader r = null; // cooked reader
                BufferedReader br = null; // buffered for readLine()
                try {
                    String s;
                    ins = new FileInputStream(file);
                    r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
                    br = new BufferedReader(r);
                    while ((s = br.readLine()) != null) {
//                        System.out.println(s);
                        lines.add(s);
                    }
                }
                catch (Exception e)
                {
//                    System.err.println(e.getMessage()); // handle exception
                }
                finally {
                    if (br != null) { try { br.close(); } catch(Throwable t) { /* ensure close happens */ } }
                    if (r != null) { try { r.close(); } catch(Throwable t) { /* ensure close happens */ } }
                    if (ins != null) { try { ins.close(); } catch(Throwable t) { /* ensure close happens */ } }
                }

                while (isRunning){
                    for (String line:lines
                         ) {
                        String[] split = line.split(",");
                        JSONObject object = new JSONObject();
                        object.put("fields_set", split[0]);
                        object.put("time_local", split[1]);
                        object.put("cookie_user_name", split[2]);
                        object.put("ua_os_name", split[3]);
                        object.put("x_forwarded_for", split[4]);
                        object.put("request", split[5]);
                        object.put("status", split[6]);
                        object.put("request_time", Double.valueOf(split[7]));
                        object.put("request_length", Double.valueOf(split[8]));
//                        System.out.println(line);
                        Thread.sleep(500);
                        sourceContext.collect(object.toJSONString());
                    }

                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        source.print();
        source.addSink(new FlinkKafkaProducer<String>("172.24.10.5:9092","nginx_user_log",new SimpleStringSchema()));

        env.execute();
    }
}
