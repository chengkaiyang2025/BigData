package com.yzf.anticrawler.util;

import com.yzf.accounting.common.core.utils.security.RSACoderUtil;
import com.yzf.accounting.common.core.utils.security.UrlDesEncodUtil;
import com.yzf.anticrawler.bean.RequestCnt;
import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：相关参数工具类代码来自刘思远
 * @date ：2021/1/29 下午5:37
 */


public class CSMMessageSender {
    // 添加header签名
    public static Map<String,String> getHeaderInfos(){
        // channel, privateKey不要改动
        String channel = "anticrawler";
        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCEckwSLe9qsx84PuKpn3kOhlGeAkSeFd6zadPNpu0A+tlZ6Isn+y+8m/h3kT9SEZ+BEdzmAtrqooh2uHLCZFPCkR6Oo+4J6TkdXIe6r3upLjMYd3efnFif0w0rFipDal5bGECqblyXFJsr3SSxnuOImUwoDNVpG7/fNIUX55txc8Co5+HZDBsb1Y7+nTuslyi+oaIfH1VidRE9oFbRvD0vdkvVyiQG/7SvDJ9EOMuH8yJUd8htfVDtMHRqgenJusgmhQTQfCHq8JaVMbQ52c52Sc0PPP6LICMGi8YbTSjsmMyozOc9CL1O0w+vJgjEKTMB9T3whtlac3sLm/7PWfpHAgMBAAECggEAf6N+uA5YqmVQ8u13lqw/B8ZeJX5eDlNZOWIFYi06PjWA/WpjoPYrfQJ6ow/xC/A73TEPxwJ9Yp2cEX2T6s6xAdLEtgY5QofmKOuhSkIPVG1XhH6tY75SkkPcbud/d1Ss9fEbcKPGyzpqiA0obZyH/n5trDMh/5dkg4CjuvPTE7NsyA2e6yz5BdONZZAdbJwpIS0+YwUexN++sq7ZDnb//63zGILLD33gR4g5PRDQwzVY/Ue3ufyGChfwOkE2s+l+4p8cpXEZzsXsbYR0CzTyRZQ9REWaixgEp6q5W2rxrPwkG+YeCEVKTVL1X6jQ/23GjUQtJlyEhejAEar3yz6qwQKBgQC6thpmnhJ5evPk13snlciCQVa2baNyWkQhmfMsjDAX3Nuv2BEgFGrt9saI8G2oZNGguD4ytAip/4XnGpC5Ugsr4eOPELW9Jr2nWLtu2r0PI34nG0oCqLZGWQhqFIJpszHiGompA6UOQKfBntZjtrwdx4mFL0mK2Uq0XPGGXkewMQKBgQC1mPBT8039BxiQ6Y6U/4bPbC+kwDtf/Ou7OFJlXziSinWAhh4bbF0Vn9BgIAQenymH9Ik0TtgUftL0SlGCOgal+KWlXYgOdqbc5rTMo/lrsWwh2lpZOs5issCmVN/BRocFslYbhrBnIXERZi0PIIOel1o9+COa4CXbmIOqhhLr9wKBgQCtiDjrbTlAfP0XkQqelQDhxzZ/oVELJEg0brBrk/PTCeuI+KmaBkAH4ZdOiR9yJhoLZZIxb9Cpp5rdW3xtMYq+g1kLi/1Nyybz1W2HL3gODO/gCuXzqLiYk4FVjrBS60COpBmT45ZRs9f3Bdi+noUirGTtSzDwAtsC3WuBdjjs0QKBgAFLRLMkcbLS4R7vOx/erkQ+HhzLbeDiOX/NB/FG+2035Vx0GDTT0dn8mIyGzeC1+6LHYE7qHOoQeUpaWThXV4LJQczq3LRVrhy23CT1KirVSdL37lria2QSqALHagzDhuTIf4qUrB8gAuE/3CRWc/mmVHNKmF5bKInnDVEiwXHDAoGBAJH3RXt8DWC1OoePLzSqWF+NfwZdoucR4QxB/CoIpOXWJW6s46nSuWoGra92442tjDHMrwd8733IfcVDCJvNEpN8XCiERelVhSWSsIrv2uWYIJCmL0WXq19DhExefkwkEkpNnL6c7XyzJRyboWsfC22JD/9Vskp8TDM0xyZG7XGN";

        String privateKeyBase64="";
        long timestamp = System.currentTimeMillis();
        String signStr = channel + "|" + timestamp;
        byte[] encryptByPrivateKey = null;
        try {
            encryptByPrivateKey= RSACoderUtil.encryptByPrivateKey(signStr.getBytes(), privateKey);
            privateKeyBase64 = UrlDesEncodUtil.byte2Base64StringFun(encryptByPrivateKey);
        } catch (Exception e) {
//            String ex = ExceptionUtil.stacktraceToString(e);
            throw new RuntimeException("加密异常!");
        }

        Map<String,String> headers = new HashMap<>();
        headers.put("csm-sign", privateKeyBase64);
        headers.put("csm-timestamp", String.valueOf(timestamp));
        headers.put("csm-channel", channel);
        return headers;
    }


//    public static void sendMessage(RequestCnt requestCnt) throws IOException {
//        CloseableHttpClient client = HttpClients.createDefault();
//
//
//        HttpPost httpPost = new HttpPost(Configuration.PROPERTIES.getProperty("csm_alarm_link"));
//
//        CSMRequestBody csmRequestBody = new CSMRequestBody("按用户、接口维度统计访问频率",getDesc(requestCnt),Integer.valueOf(requestCnt.getGsId()),requestCnt.getLastTime());
//
//        String json = JSON.toJSONString(csmRequestBody);
//        StringEntity entity = new StringEntity(json, "UTF-8");
//        httpPost.setEntity(entity);
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
//        Map<String,String> csmHeader = CSMMessageSender.getHeaderInfos();
//        for(String key:csmHeader.keySet()){
//            httpPost.setHeader(key,csmHeader.get(key));
//        }
//
//        CloseableHttpResponse response = client.execute(httpPost);
//        String responseBody = EntityUtils.toString(response.getEntity());
//        System.out.println(responseBody);
//        client.close();
//    }
}
