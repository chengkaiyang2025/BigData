package com.yzf.di.newdzlogsinkhive.stream;

import com.yzf.di.newdzlogsinkhive.bean.LogBean;
import com.yzf.di.newdzlogsinkhive.bean.TokenInfo;
import com.yzf.di.newdzlogsinkhive.bean.UserAgent;
import com.yzf.di.newdzlogsinkhive.util.CookieUtil;
import com.yzf.di.newdzlogsinkhive.util.TimeFormatUtil;
import com.yzf.di.newdzlogsinkhive.util.TokenInfoUtil;
import com.yzf.di.newdzlogsinkhive.util.UserAgentUtil;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 由于 cookie的解析需要设计较复杂的decode过程，如果每来一条链接
 * 都调用decode方法，
 * 经2021年3月17日离线统计：
 * 总请求为一亿2千万请求
 * 20210315去重http_cookie 为1009715，其中set1最活跃，共25万个。如果使用状态编程，则cookie decode的调用量减少到百分之一
 * 策略：按照set作为key，每个Key分区中定义一个MapState，存储cookie。
 * 关于ttl的设置见gitlab上的issue：http://gitlab.yzf.net/group_di/bigdata/flink-job/issues/15
 */
public class DecodeCookieMapper extends KeyedProcessFunction<Integer, LogBean, LogBean> {
    Logger logger = LoggerFactory.getLogger(NginxAnalysisStream.class);
    // 如果跑历史数据，这里必须设置比较少，保证状态后端中的cookie最多只有几万个
    private final int cookieTTLMinute = 5;
    private MapState<String, TokenInfo> cookieMapState;
    private final int uaTTLMinute = 10;
    private MapState<String, UserAgent> uaMapState;
    private ValueState<Integer> cookieCntState;
    private ValueState<Integer> uaCntState;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 初始化参数，当cookieMapState中缓存的cookieKV数量达到 cookieClearSize时候 cookieTTLMinute, uaTTLMinute
//    public DecodeCookieMapper(int cookieTTLMinute,int uaTTLMinute) {
//        this.cookieTTLMinute = cookieTTLMinute;
//        this.uaTTLMinute = uaTTLMinute;
//    }

    @Override
    public void processElement(LogBean logBean, Context context, Collector<LogBean> collector) {
        // TODO 问题可能出现在这里，不断的创建对象，应该在source端就接写好 BUG点
//        try {
//            logBean.setTime_local_ts(TimeFormatUtil.transformer2(logBean.getTime_local()));
//        }catch (Exception e){
////            e.printStackTrace();
//        }
//
        try {
            logBean.setTime_local_ts(TimeFormatUtil.transformer2(logBean.getTime_local()));

            TokenInfo decode;

            String cookieString = logBean.getHttp_cookie();
            String access_token = CookieUtil.getCookieValueByCookieKey(cookieString, "access_token");

            if(cookieMapState.contains(access_token)){
                decode = cookieMapState.get(access_token);
//                System.out.println(sdf.format(context.timerService().currentProcessingTime())+"命中");

            }else {
                // 解析成为TokenInfo的pojo类

                decode = TokenInfoUtil.decode(access_token);
                cookieMapState.put(access_token,decode);

                // TODO 统计操作数
//                Integer count = cookieCntState.value();
//                if(count == null) count = 0;
//                ++count;
//                cookieCntState.update(count);
//                // 调度一万次打印一次
////                if(count % 2000 == 0){
//                    System.out.println(sdf.format(context.timerService().currentProcessingTime()) + ": key为"
//                            + context.getCurrentKey()+":当前cookie累计解析次数:"+count);
////                }
//                Iterator<Map.Entry<String, TokenInfo>> iterator = cookieMapState.iterator();
//                System.out.println(sdf.format(context.timerService().currentProcessingTime()) + ": key为"
//                        + context.getCurrentKey()+" cookie大小为" +IteratorUtils.toList(iterator).size());


            }
            logBean.setCookie_gs_id(decode.getGsId());
            logBean.setCookie_phone(decode.getPhone());
            logBean.setCookie_user_id(decode.getUserId());
            logBean.setCookie_user_name(decode.getUserName());


        }catch (Exception e){
//            e.printStackTrace();
            // TODO 测输出流收集失败信息
        }
        try {
            // 通过UA解析处用户使用的浏览器等相关信息

            UserAgent userAgent ;

//            Integer currentKey = context.getCurrentKey();
            String uaString = logBean.getHttp_user_agent();
            if(uaMapState.contains(uaString)){
                userAgent = uaMapState.get(uaString);
            }else {
                userAgent = UserAgentUtil.get(uaString);
                uaMapState.put(uaString,userAgent);
                // 统计操作数
//                Integer count = uaCntState.value();
//                if(count == null) count = 0;
//                ++count;
//                uaCntState.update(count);
//                // 调度2次打印一次
//                if(count % 2000 == 0){
//                    System.out.println(sdf.format(context.timerService().currentProcessingTime())+":ua累计解析次数:"+count);
//                }

            }
            logBean.setUa_browser_version_info(userAgent.getBrowserVersionInfo());
            logBean.setUa_device_type(userAgent.getDeviceType());
            logBean.setUa_os_family(userAgent.getOsFamily());
            logBean.setUa_os_name(userAgent.getOsName());
            logBean.setUa_type(userAgent.getType());
            logBean.setUa_ua_family(userAgent.getUaFamily());
            logBean.setUa_ua_name(userAgent.getUaName());
        }catch (Exception e){
//            e.printStackTrace();
            // TODO 测输出流收集失败信息
        }
        collector.collect(logBean);
    }

    /**
     * @param parameters
     * @throws Exception
     *  https://ci.apache.org/projects/flink/flink-docs-release-1.12/dev/stream/state/state.html#state-time-to-live-ttl
     *   设置ttl过期时间，对于某个cookie,如果存在tokenTTLMinute后，则标记过期。
     *    This means that list elements and map entries expire independently.
     *    指mapState中的每对kv单独记录自己的时间戳。
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 1、设置MapState的过期策略：
        StateTtlConfig cookieStateTtlConfig1 = StateTtlConfig
                .newBuilder(Time.minutes(cookieTTLMinute))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp)
//                .cleanupFullSnapshot()
                .build();
        StateTtlConfig uaStateTtlConfig = StateTtlConfig
                .newBuilder(Time.minutes(uaTTLMinute))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp)
//                .cleanupFullSnapshot()
                .build();


        // 2、设置MapStateDescriptor，规定KV的类型
        MapStateDescriptor<String, TokenInfo> cookieStateDescriptor = new MapStateDescriptor<>(
                "cookie", String.class, TokenInfo.class
        );

        ValueStateDescriptor<Integer> cookieCntDescriptor = new ValueStateDescriptor<Integer>(
                "cookieCnt",Integer.class
        );

        ValueStateDescriptor<Integer> uaCntDescriptor = new ValueStateDescriptor<Integer>(
                "uaCnt",Integer.class
        );
        MapStateDescriptor<String, UserAgent> mapStateDescriptor = new MapStateDescriptor<>(
                "ua", String.class, UserAgent.class
        );
        // enable过期时间策略。
        cookieStateDescriptor.enableTimeToLive(cookieStateTtlConfig1);
        mapStateDescriptor.enableTimeToLive(uaStateTtlConfig);
        // 赋值
        cookieMapState = getRuntimeContext().getMapState(cookieStateDescriptor);
        uaMapState = getRuntimeContext().getMapState(mapStateDescriptor);
        cookieCntState = getRuntimeContext().getState(cookieCntDescriptor);
        uaCntState = getRuntimeContext().getState(uaCntDescriptor);
    }

    /**
     * 如果状态后端使用rocksdb，那么一定要注释掉以下两行代码，不注释会导致tm挂掉！
     * 相关issue记录：
     * http://gitlab.yzf.net/group_di/bigdata/flink-job/issues/18
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
//            cookieMapState.clear();
//            uaMapState.clear();
    }

}
