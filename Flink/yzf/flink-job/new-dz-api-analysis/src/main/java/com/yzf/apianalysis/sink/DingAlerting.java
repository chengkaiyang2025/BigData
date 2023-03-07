package com.yzf.apianalysis.sink;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

import java.util.Arrays;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/7/19 17:51
 * @description：
 */
public class DingAlerting {
    public static void main(String[] args) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=b415f46f82ab7da0e0459ef10b6c5089c461dd1d0182d5e49db7ebda2ef70e25");
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("api_2:Alerting");
        request.setText(text);
//        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
//        at.setAtMobiles(Arrays.asList("18851151526"));
//        at.setIsAtAll(true);
//        request.setAt(at);

        client.execute(request);
//        OapiRobotSendResponse response = client.execute(request);
    }

}
