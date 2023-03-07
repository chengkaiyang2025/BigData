package com.yzf.di.sink;

import com.yzf.di.alert.integration.client.AlertReport;
import com.yzf.di.alert.integration.common.template.DingTalkTemplate;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.types.Row;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/29 10:40
 * @description：
 */
public class DingAlertingSink extends RichSinkFunction<Row> {
    @Override
    public void invoke(Row value, Context context) throws Exception {

        String templateContent =
                "告警类型：接口调用异常预警 \n" +
                        "接口信息：$apiInfo \n" +
                        "返回错误码：$statusCode \n" +
                        "调用次数（统计区间：20s）：$invokeNum \n" +
                        "调用时间：$invokeTime \n";


        DingTalkTemplate.build(
                "apiAlerting",
                templateContent,
                // 线上群
                "57ba8f8d9e8fedf99fd330fed886d7e74c81a4693b82136424eca92f9fc8d444",
                //测试群
//                "b415f46f82ab7da0e0459ef10b6c5089c461dd1d0182d5e49db7ebda2ef70e25",
                Arrays.asList(""),
                false,
                DingTalkTemplate.MessageType.TEXT,
                "apiAlerting");

        Map<String, String> alertInfo = new HashMap<>();
        alertInfo.put("apiInfo", value.getField(0).toString());
        alertInfo.put("statusCode", value.getField(1).toString());
        alertInfo.put("invokeNum", value.getField(2).toString());
        alertInfo.put("invokeTime", value.getField(3).toString());

        AlertReport.sendByLocal("apiAlerting", alertInfo);

    }
}
