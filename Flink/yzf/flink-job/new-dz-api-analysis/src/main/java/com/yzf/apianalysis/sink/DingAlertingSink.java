package com.yzf.apianalysis.sink;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.types.Row;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/29 10:40
 * @description：
 */
public class DingAlertingSink extends RichSinkFunction<Row> {
    @Override
    public void invoke(Row value, Context context) throws Exception {
        // 内部群
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=b415f46f82ab7da0e0459ef10b6c5089c461dd1d0182d5e49db7ebda2ef70e25");
        // 上线预警群
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=ad30d97b104d081b6c69b1d802d9b6ba0d3b5ee5aaae266b46dd19e6f6e92920");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text apiAlertingInfo = new OapiRobotSendRequest.Text();
        apiAlertingInfo.setContent("告警类型：接口调用异常预警 \n" +
                "接口信息：" + value.getField(0) + "\n" +
                "返回错误码：" + value.getField(1) + "\n" +
                "调用次数（统计区间：20s）：" + value.getField(2) + "\n" +
                "调用时间：" + value.getField(3));

        request.setText(apiAlertingInfo);

        client.execute(request);
    }
}
