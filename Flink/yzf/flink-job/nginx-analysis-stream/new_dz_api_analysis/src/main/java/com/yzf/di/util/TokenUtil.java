package com.yzf.di.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yzf.di.bean.TokenInfo;

import java.util.Date;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/8/13 16:05
 * @description：
 */
public class TokenUtil {

    public static final String USERNAME = "userName";
    public static final String GSID = "gsId";
    public static final String USERID = "userId";
    public static final String PHONE = "phone";
    public static final String SET = "set";

    private static TokenInfo getTokenInfo(DecodedJWT jwt) throws NullPointerException {
        TokenInfo tokenInfo = null;
        if (jwt != null) {
            String userName = jwt.getClaim(USERNAME).asString();
            String gsId = jwt.getClaim(GSID).asString();
            String userId = jwt.getClaim(USERID).asString();
            String phone = jwt.getClaim(PHONE).asString();
            String set = jwt.getClaim(SET).asString();

            tokenInfo = new TokenInfo();
            tokenInfo.setGsId(gsId);
            tokenInfo.setPhone(phone);
            tokenInfo.setSet(set);
            tokenInfo.setUserId(userId);
            tokenInfo.setUserName(userName);
        }
        return tokenInfo;
    }

    public static TokenInfo decode(String accessToken) {
        TokenInfo tokenInfo = new TokenInfo();
        try {
            if (accessToken != null && accessToken.length() > 0) {
                DecodedJWT jwt = JWT.decode(accessToken);
                tokenInfo = getTokenInfo(jwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenInfo;
    }


}
