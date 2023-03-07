package com.atguigu.hotitems_analysis;

import com.atguigu.hotitems_analysis.bean.ItemViewCount;
import com.atguigu.hotitems_analysis.bean.UserBehavior;
import org.apache.commons.compress.utils.Lists;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;

public class HotItems {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        String file = "D:\\IdeaProject\\Bigdata\\Flink\\UserBehaviorAnalysis\\HotItemsAnalysis\\src\\main\\resources\\UserBehavior.csv";
        SingleOutputStreamOperator<UserBehavior> source
                = env.readTextFile(file).map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String value) throws Exception {
                String[] fields = value.split(",");
                return new UserBehavior(
                        new Long(fields[0]), new Long(fields[1]), new Integer(fields[2]), fields[3], new Long(fields[4])
                );
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<UserBehavior>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(UserBehavior element) {
                return element.getTimestamp()*1000L;
            }
        });

        SingleOutputStreamOperator<ItemViewCount> itemId = source.filter(data -> "pv".equals(data.getBehavior()))
                .keyBy("itemId")
                .timeWindow(Time.hours(1), Time.minutes(5))
                .aggregate(new ItemCountAgg(), new WindowItemCountResult());
        SingleOutputStreamOperator<String> windowEnd = itemId.keyBy("windowEnd")
                .process(new TopNHtopItems(5));
        windowEnd.print();
        env.execute();
    }
    public static class ItemCountAgg implements AggregateFunction<UserBehavior,Long,Long>{
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(UserBehavior value, Long accumulator) {
            return accumulator + 1;
        }

        @Override
        public Long getResult(Long accumulator) {
            return accumulator;
        }

        @Override
        public Long merge(Long a, Long b) {
            return a+b;
        }
    }
    public static class WindowItemCountResult implements WindowFunction<Long, ItemViewCount, Tuple, TimeWindow>{
        @Override
        public void apply(Tuple tuple, TimeWindow window, Iterable<Long> input, Collector<ItemViewCount> out) throws Exception {
            Long itemId = tuple.getField(0);
            Long windowEnd = window.getEnd();
            Long count = input.iterator().next();

            out.collect(new ItemViewCount(itemId,windowEnd,count));
        }
    }
    public static class TopNHtopItems extends KeyedProcessFunction<Tuple, ItemViewCount,String> {
        private Integer topSize;
        ListState<ItemViewCount> itemViewCountListState;
        @Override
        public void open(Configuration parameters) throws Exception {
            itemViewCountListState = getRuntimeContext().getListState(new ListStateDescriptor<ItemViewCount>("item-view-count-list", ItemViewCount.class));
        }

        @Override
        public void close() throws Exception {
            itemViewCountListState.clear();
            super.close();
        }
        public TopNHtopItems(Integer topSize){
            this.topSize = topSize;
        }
        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {

            ArrayList<ItemViewCount> itemViewCounts = Lists.newArrayList(itemViewCountListState.get().iterator());

            itemViewCounts.sort(new Comparator<ItemViewCount>() {
                @Override
                public int compare(ItemViewCount o1, ItemViewCount o2) {
                    return o2.getCount().intValue() - o1.getCount().intValue();
                }
            });

            StringBuilder sb = new StringBuilder();
            sb.append("===================================\n");
            sb.append("窗口结束时间：").append(new Timestamp(timestamp - 1)).append("\n");

            for(int i = 0;i<Math.min(topSize,itemViewCounts.size());i++){
                ItemViewCount itemViewCount = itemViewCounts.get(i);
                sb.append("NO ").append(i+1).append(":")
                        .append("商品ID = ").append(itemViewCount.getItemId())
                        .append(" 热门度 = ").append(itemViewCount.getCount())
                        .append("\n");
            }
            sb.append("===================================\n");
            Thread.sleep(1000L);

            out.collect(sb.toString());
        }

        @Override
        public void processElement(ItemViewCount value, Context ctx, Collector<String> out) throws Exception {
            itemViewCountListState.add(value);
            ctx.timerService().registerEventTimeTimer(value.getWindowEnd() + 1);
        }
    }
}
