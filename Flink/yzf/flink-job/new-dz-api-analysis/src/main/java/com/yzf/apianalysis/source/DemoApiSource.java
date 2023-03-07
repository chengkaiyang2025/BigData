package com.yzf.apianalysis.source;

import com.yzf.apianalysis.beans.ApiInfo;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Random;


/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/19 16:32
 * @description：
 */
public class DemoApiSource implements SourceFunction<String> {
    private boolean running = true;

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        Random random = new Random();
        HashMap<String, Integer> apiInfoTempMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            apiInfoTempMap.put("api_" + (i + 1), random.nextInt(10));
        }

        while (running) {
            for (String apiId : apiInfoTempMap.keySet()) {
                String result = new ApiInfo(apiId, apiInfoTempMap.get(apiId) + random.nextInt(10), System.currentTimeMillis()).toString();
                ctx.collect(result);
            }
            Thread.sleep(2000L);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

}
