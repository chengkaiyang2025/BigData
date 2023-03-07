package com.atguigu.apitest.util;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

import com.atguigu.apitest.beans.TokenInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author ：yangchengkai@yunzhangfang.com
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

        return tokenInfo;
    }

}
