package com.atguigu.practice.util;


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
        return parseCookie(cookieRawString).get(cookieKey);
    }
    private static Map<String,String> parseCookie(String cookieRawString) {
        Map<String,String> cookieMap = new HashMap<String, String>();
        Pattern cookiePattern = Pattern.compile("([^=]+)=([^\\;]*);?\\s?");
        Matcher matcher = cookiePattern.matcher(cookieRawString);
        while (matcher.find()) {
            String cookieKey = matcher.group(1);
            String cookieValue = matcher.group(2);
            cookieMap.put(cookieKey, cookieValue);
        }
        return cookieMap;
    }
}
