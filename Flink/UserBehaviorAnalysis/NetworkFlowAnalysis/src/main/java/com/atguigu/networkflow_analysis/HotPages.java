package com.atguigu.networkflow_analysis;

import com.atguigu.networkflow_analysis.beans.ApacheLogEvent;
import com.atguigu.networkflow_analysis.beans.PageViewCount;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

public class HotPages {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        String file = "D:\\IdeaProject\\Bigdata\\Flink\\UserBehaviorAnalysis\\NetworkFlowAnalysis\\src\\main\\resources\\apache.log";
        DataStreamSource<String> source = env.readTextFile(file);
        SingleOutputStreamOperator<ApacheLogEvent> sourceEvent = source.map(new MapFunction<String, ApacheLogEvent>() {
            @Override
            public ApacheLogEvent map(String s) throws Exception {
                String[] fields = s.split(" ");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
                long timestamp = simpleDateFormat.parse(fields[3]).getTime();
                return new ApacheLogEvent(fields[0], fields[1], timestamp, fields[5], fields[6]);

            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<ApacheLogEvent>(Time.seconds(1)) {
            @Override
            public long extractTimestamp(ApacheLogEvent element) {
                return element.getTimestamp();
            }
        });

        // 定义一个侧输出流
        OutputTag<ApacheLogEvent> outputTag = new OutputTag<ApacheLogEvent>("late"){};

        // 过滤与url计数
        SingleOutputStreamOperator<PageViewCount> aggregate = sourceEvent.filter(data -> "GET".equals(data.getMethod()))    // 过滤get请求
                .filter(data -> {
                    String regex = "^((?!\\.(css|js|png|ico)$).)*$";
                    return Pattern.matches(regex, data.getUrl());
                }).keyBy(ApacheLogEvent::getUrl)
                .timeWindow(Time.minutes(10), Time.seconds(5))
                .allowedLateness(Time.minutes(1))
                .sideOutputLateData(outputTag)
                .aggregate(new PageCountAgg(), new PageCountResult());
        SingleOutputStreamOperator<String> process = aggregate.keyBy(PageViewCount::getWindowEnd)
                .process(new TopNHtopPage(3));
        // 收集同一个窗口的数据，然后计算topN
        process.print();
        env.execute();
    }
    public static class TopNHtopPage extends KeyedProcessFunction<Long,PageViewCount,String>{
        private Integer topSize;
        public TopNHtopPage(Integer topSize){
            this.topSize = topSize;
        }
        MapState<String,Long> pageViewMapState;
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
//          super.onTimer(timestamp, ctx, out);
            if(timestamp == ctx.getCurrentKey()+60*1000L){
                pageViewMapState.clear();
                return;
            }
            ArrayList<Map.Entry<String, Long>> entries = Lists.newArrayList(pageViewMapState.entries());
            entries.sort(new Comparator<Map.Entry<String, Long>>() {
                @Override
                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                    if(o1.getValue() > o2.getValue()){
                        return -1;
                    }else if(o1.getValue() < o2.getValue()){
                        return 1;
                    }else
                        return 0;
//                    return 0;
                }
            });
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("===================================");
            stringBuffer.append("窗口函数").append(new Timestamp(timestamp -1)).append("\n");

            for (int i = 0; i < Math.min(topSize, entries.size()); i++) {
                Map.Entry<String, Long> stringLongEntry = entries.get(i);
                stringBuffer.append("NO ").append(i+1).append(":")
                        .append("页面URL：=").append(stringLongEntry.getKey())
                        .append("浏览量= ").append(stringLongEntry.getValue())
                        .append("\n");
            }
            stringBuffer.append("===================================");

            // 控制输出频率
            Thread.sleep(1000L);

            out.collect(stringBuffer.toString());
        }

        @Override
        public void close() throws Exception {
            pageViewMapState.clear();
        }

        @Override
        public void open(Configuration parameters) throws Exception {
//
//            super.open(parameters);
            pageViewMapState = getRuntimeContext().getMapState(new MapStateDescriptor<String, Long>("page-view",String.class,Long.class));
        }

        @Override
        public void processElement(PageViewCount value, Context ctx, Collector<String> out) throws Exception {
            pageViewMapState.put(value.getUrl(),value.getCount());
            ctx.timerService().registerEventTimeTimer(value.getWindowEnd()+1L);
            ctx.timerService().registerEventTimeTimer(value.getWindowEnd()+60*1000L);
        }
    }
    public static class PageCountAgg implements AggregateFunction<ApacheLogEvent,Long,Long>{
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(ApacheLogEvent apacheLogEvent, Long aLong) {
            return aLong+1;
        }

        @Override
        public Long getResult(Long aLong) {
            return aLong;
        }

        @Override
        public Long merge(Long aLong, Long acc1) {
            return aLong+acc1;
        }
    }
    public static class PageCountResult implements WindowFunction<Long, PageViewCount, String, TimeWindow> {
        @Override
        public void apply(String s, TimeWindow window, Iterable<Long> input, Collector<PageViewCount> out) throws Exception {
            out.collect(new PageViewCount(s,window.getEnd(),input.iterator().next()));
        }
    }
}
