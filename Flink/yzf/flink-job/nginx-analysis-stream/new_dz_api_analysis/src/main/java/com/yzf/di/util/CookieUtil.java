package com.yzf.di.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/8/13 16:05
 * @description：
 */
public class CookieUtil {

    public static String getCookieValueByCookieKey(String cookieRawString, String cookieKey) {
        return parseCookie(cookieRawString).get(cookieKey);
    }

    private static Map<String, String> parseCookie(String cookieRawString) {
        Map<String, String> cookieMap = new HashMap<String, String>();
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
