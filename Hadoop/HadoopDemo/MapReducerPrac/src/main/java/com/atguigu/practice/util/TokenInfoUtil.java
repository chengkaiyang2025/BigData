package com.atguigu.practice.util;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

import com.atguigu.practice.bean.TokenInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：代码来自于git仓库：
 * http://172.23.40.145/yangchengkai/token-parser/blob/master/src/main/java/com/yzf/support/token/tokenparser/dto/TokenInfo.java
 * 此代码段 clone自 http://172.23.40.145/architecture-group/token-parser/tree/master
 * 主要解决ng中cookie的access_token字段解析问题。
 * @date ：2021/1/28 下午4:53
 */


public class TokenInfoUtil {

     public static final String USERNAME = "userName";

    public static final String GSID = "gsId";

    public static final String USERID = "userId";

    public static final String PHONE = "phone";

    public static final String SET = "set";


    public static TokenInfo decode(String accessToken) throws JWTDecodeException {
        DecodedJWT jwt = JWT.decode(accessToken);

        return getTokenInfo(jwt);

    }


    private static TokenInfo getTokenInfo(DecodedJWT jwt) {
//        Logger log = LoggerFactory.getLogger(TokenInfoUtil.class);

        TokenInfo tokenInfo = null;
        if (jwt != null) {
            String userName = jwt.getClaim(USERNAME).asString();
            String gsId = jwt.getClaim(GSID).asString();
            String userId = jwt.getClaim(USERID).asString();
            String phone = jwt.getClaim(PHONE).asString();
            String set = jwt.getClaim(SET).asString();
            String iss = jwt.getIssuer();
            Date expiredDate = jwt.getExpiresAt();

            tokenInfo = new TokenInfo();
            tokenInfo.setGsId(gsId);
            tokenInfo.setPhone(phone);
            tokenInfo.setSet(set);
            tokenInfo.setUserId(userId);
            tokenInfo.setUserName(userName);
            tokenInfo.setIss(iss);

            DateTime dateTime = new DateTime(expiredDate);
            String expiredTime = dateTime.toString("yyyy-MM-dd HH:mm:ss");

            tokenInfo.setExpiredTime(expiredTime);
        }

//        log.debug("tokeninfo:{}", tokenInfo);
        return tokenInfo;
    }


//    private boolean isExpired(DecodedJWT jwt) {
//        Date date = jwt.getExpiresAt();
//        DateTime expireTime = new DateTime(date);
//        long currentTime = System.currentTimeMillis();
//        // log.info("expireTime:{}  currentTime:{}", expireTime, currentTime);
//        if (expireTime.isBefore(currentTime)) {
//            return true;
//        } else {
//            return false;
//        }
//    }


//    public Map<String, Object> introspect(String accessToken) throws UnsupportedEncodingException {
//
//        DecodedJWT jwt = JWT.decode(accessToken);
//        TokenInfo tokenInfo = getTokenInfo(jwt);
//
//        boolean isExpired = isExpired(jwt);
//
//        Env env = verifySignature(splitToken(accessToken));
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", tokenInfo);
//        map.put("isExpired", isExpired);
//        map.put("env", env);
//        // log.info("{}", map);
//        return map;
//    }
//
//
//    private Env verifySignature(String[] parts) throws SignatureVerificationException, UnsupportedEncodingException {
//
//
//        byte[] content = String.format("%s.%s", parts[0], parts[1]).getBytes(StandardCharsets.UTF_8);
//        byte[] signature = Base64.decodeBase64(parts[2]);
//
//        try {
//            devAlgo.verify(content, signature);
//            return new Env("dev", "【企业版联调环境(http://sso.devn.yzf.net)，钉钉扫码测试环境(http://adminsso.yzf.net)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not dev");
//        }
//
//
//        try {
//            ceshiAlgo.verify(content, signature);
//            return new Env("ceshi", "【代账测试环境(134,187)，企业版测试环境(http://sso.fat.yzf.net)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not ceshi");
//        }
//
//        try {
//            deskAlgo.verify(content, signature);
//            return new Env("desk", "【钉钉扫码线上环境(https://adminsso.yunzhangfang.com)】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not desk");
//        }
//
//
//        try {
//            proAlgo.verify(content, signature);
//            return new Env("product", "【代账，企业版线上环境】");
//        } catch (SignatureVerificationException se) {
//            // log.info("not product");
//        }
//
//        return new Env("unknown", "未知的签名");
//    }
//


    /**
     * Splits the given token on the "." chars into a String array with 3 parts.
     *
     * @param token the string to split.
     * @return the array representing the 3 parts of the token.
     * @throws JWTDecodeException if the Token doesn't have 3 parts.
     */
//    private String[] splitToken(String token) throws JWTDecodeException {
//        String[] parts = token.split("\\.");
//        if (parts.length == 2 && token.endsWith(".")) {
//            //Tokens with alg='none' have empty String as Signature.
//            parts = new String[]{parts[0], parts[1], ""};
//        }
//        if (parts.length != 3) {
//            throw new JWTDecodeException(String.format("The token was expected to have 3 parts, but got %s.", parts.length));
//        }
//        return parts;
//    }
}
