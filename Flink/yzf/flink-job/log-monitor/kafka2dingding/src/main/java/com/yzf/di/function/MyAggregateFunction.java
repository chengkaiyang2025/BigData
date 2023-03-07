package com.yzf.di.function;

import com.yzf.di.bean.LogMonitorBean;
import com.yzf.di.bean.LogMonitorBeans;
import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/9 16:15
 */
public class MyAggregateFunction implements AggregateFunction<LogMonitorBean, Map<String, LogMonitorBean>, LogMonitorBeans> {
    @Override
    public Map<String, LogMonitorBean> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    /**
     * @param logMonitorBean
     * @param map
     * @return key :ip, value: 同一个IP的所有LogMonitorBean
     */
    @Override
    public Map<String, LogMonitorBean> add(LogMonitorBean logMonitorBean, Map<String, LogMonitorBean> map) {
        if (map.containsKey(logMonitorBean.getIp())) {
            LogMonitorBean logMonitorBean1 = map.get(logMonitorBean.getIp());
            logMonitorBean1.addAll(logMonitorBean.getErrorLogs());
            map.put(logMonitorBean1.getIp(), logMonitorBean1);
        } else {
            map.put(logMonitorBean.getIp(), logMonitorBean);
        }

        return map;
    }


    @Override
    public LogMonitorBeans getResult(Map<String, LogMonitorBean> map) {
        LogMonitorBeans logMonitorBeans = new LogMonitorBeans();
        Iterator<LogMonitorBean> iterator = map.values().iterator();
        map.values().forEach(bean -> {
            logMonitorBeans.setType(bean.getType());
            logMonitorBeans.add(bean);
        });

        return null;
    }

    /**
     * 合并两个map
     *
     * @param map
     * @param map2
     * @return
     */
    @Override
    public Map<String, LogMonitorBean> merge(Map<String, LogMonitorBean> map, Map<String, LogMonitorBean> map2) {
        Map<String, LogMonitorBean> result = new ConcurrentHashMap<>(map);
        for (Map.Entry<String, LogMonitorBean> entry : map2.entrySet()) {
            if (map.containsKey(entry.getKey())) {
                LogMonitorBean logMonitorBean = map.get(entry.getKey());
                logMonitorBean.addAll(entry.getValue().getErrorLogs());
                result.put(entry.getKey(), logMonitorBean);
            }
        }
        return result;
    }
}
