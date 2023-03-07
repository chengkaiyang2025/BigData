package com.yzf.di.newdzlogsinkhive.util;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从Cookie的字符串中得到Cookie的KV
 * @date ：2021/1/28 下午8:22
 */


public class CookieUtil {
    public static String getCookieValueByCookieKey(String cookieRawString,String cookieKey) {
        return parseCookie(cookieRawString,cookieKey);
    }
    private static String parseCookie(String cookieRawString,String cookieKey) {
        Map<String,String> cookieMap = new HashMap<String, String>();
        Pattern cookiePattern = Pattern.compile("([^=]+)=([^\\;]*);?\\s?");
        Matcher matcher = cookiePattern.matcher(cookieRawString);

        while (matcher.find()) {
            if(cookieKey.equals(matcher.group(1))){
                return matcher.group(2);
            }
        }
        return null;
    }
}
