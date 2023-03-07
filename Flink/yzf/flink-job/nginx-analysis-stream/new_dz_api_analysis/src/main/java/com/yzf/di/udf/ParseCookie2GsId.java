package com.yzf.di.udf;

import com.yzf.di.bean.TokenInfo;
import com.yzf.di.util.CookieUtil;
import com.yzf.di.util.TokenUtil;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/8/13 16:13
 * @description：
 */
public class ParseCookie2GsId extends ScalarFunction {
    public String eval(String cookie) {
        if (cookie.equals(null)) {
            return "";
        } else {
            String accessToken = CookieUtil.getCookieValueByCookieKey(cookie, "access_token");
            TokenInfo tokenInfo = TokenUtil.decode(accessToken);
            return tokenInfo.getGsId();
        }
    }

}
