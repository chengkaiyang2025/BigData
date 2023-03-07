package com.yzf.di.util;

import com.yzf.di.bean.TokenInfo;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/8/13 16:17
 * @description：
 */
public class TokenUtilTest {

    @Test
    public void testTokenUtil() {
        String cookie = "_ga=GA1.2.58633216.1614937137; _gid=GA1.2.2054963949.1628556246; Hm_lvt_5b07def52c26a375c2ec8059668f1c81=1628556246,1628642171,1628728576,1628814931; Hm_lpvt_5b07def52c26a375c2ec8059668f1c81=1628814931; Hm_lvt_63aaa8171e4ecaa960e37f8eff820330=1628556252,1628642179,1628728584,1628814942; refresh_token=a2b91cb299b7488e9f6ed5e859745bb4; access_token=eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzOTkiLCJwaG9uZSI6IjE1MTYxNDcwOTEzIiwiZ3NJZCI6IjI2OCIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJ6bHkwMDEiLCJleHAiOjE2Mjg4MjAzNzcsInVzZXJJZCI6IjcwNDYifQ.ygtdRICJar1HO9l46kLdG67y55qZUqa58aDxZzGqw18; tgw_l7_route=8b076e92a8d21884865f8d8def9dc818; Hm_lpvt_63aaa8171e4ecaa960e37f8eff820330=1628820292";
        String cookieValue = CookieUtil.getCookieValueByCookieKey(cookie, "access_token");
        TokenInfo tokenInfo = TokenUtil.decode(cookieValue);
        System.out.println(tokenInfo);
        System.out.println(tokenInfo.getGsId());

    }
}