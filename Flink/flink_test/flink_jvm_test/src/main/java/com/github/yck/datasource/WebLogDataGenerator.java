package com.github.yck.datasource;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 模拟日志生成流
 */
public class WebLogDataGenerator implements SourceFunction<String> {
    public static final String WEB_PREFIX = "http://www.baidu.com/";
    public static final String WEB_INDEX = ".index";

    /**
     * @param peopleCnt 网站最多有多少用户
     * @param DataGenerateGapMilleSeconds 每隔多少毫秒生成一条数据。
     * @param dataGenerateStrategy 数据生成测率
     */
    public WebLogDataGenerator(int peopleCnt, int DataGenerateGapMilleSeconds, DataGenerateStrategy dataGenerateStrategy) {
        this.peopleCnt = peopleCnt;
        this.DataGenerateGapMilleSeconds = DataGenerateGapMilleSeconds;
        this.dataGenerateStrategy = dataGenerateStrategy;
        r = new Random();
    }

    private int peopleCnt;
    private DataGenerateStrategy dataGenerateStrategy;
    private int DataGenerateGapMilleSeconds;
    private Random r;

    /**
     * while true 放在本地私有方法中，防止循环中一直判断dataGenerateStrategy
     * @param ctx The context to emit elements to and for accessing locks.
     * @throws Exception
     */
    @Override
    public void run(SourceContext<String> ctx) throws Exception {

        switch (dataGenerateStrategy){
            case FIXED_NUMBER:generateWebLogFixed(ctx,peopleCnt,DataGenerateGapMilleSeconds);break;
            case RANDOM_NUMBER:generateWebLogRandomly(ctx,peopleCnt,DataGenerateGapMilleSeconds);break;
            default:break;
        }

    }

    /**
     * @param ctx
     * @param peopleCnt
     * @param dataGenerateGapMilleSeconds
     */
    private void generateWebLogFixed(SourceContext<String> ctx, int peopleCnt, int dataGenerateGapMilleSeconds) throws InterruptedException {
        while (true){
            ctx.collect(WEB_PREFIX+r.nextInt(peopleCnt)+WEB_INDEX);
            TimeUnit.MILLISECONDS.sleep((DataGenerateGapMilleSeconds));
        }
    }

    /**
     * 在间隔内随机生成数据
     * @param ctx
     * @param peopleCnt
     * @param DataGenerateGapMilleSeconds
     */
    private void generateWebLogRandomly(SourceContext<String> ctx, int peopleCnt, int DataGenerateGapMilleSeconds) throws InterruptedException {
        while (true){
            ctx.collect(WEB_PREFIX+r.nextInt(peopleCnt)+WEB_INDEX);
            TimeUnit.MILLISECONDS.sleep(r.nextInt(DataGenerateGapMilleSeconds));
        }
    }


    @Override
    public void cancel() {

    }
}
