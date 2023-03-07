package com.yzf.di.function;

import com.yzf.di.bean.LogMonitorBeans;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @Author: MaJiBin
 * @Date: 2021/8/10 14:47
 */
public class MyReduceFunction implements ReduceFunction<LogMonitorBeans> {
    @Override
    public LogMonitorBeans reduce(LogMonitorBeans value1, LogMonitorBeans value2) throws Exception {
        LogMonitorBeans logMonitorBeans = new LogMonitorBeans()
                .addLogMonitorBeans(value1)
                .addLogMonitorBeans(value2);
        logMonitorBeans.setType(value1.getType());
        return logMonitorBeans;
    }
}
