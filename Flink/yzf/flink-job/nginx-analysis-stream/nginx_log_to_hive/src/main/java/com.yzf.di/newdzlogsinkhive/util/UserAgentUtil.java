package com.yzf.di.newdzlogsinkhive.util;

import com.yzf.di.newdzlogsinkhive.bean.UserAgent;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

import java.io.IOException;

/**
 * Created by Edward on 2016/7/1.
 */
public class UserAgentUtil {

    static UASparser uasParser = null;

    // 初始化uasParser对象
    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserAgent get(String userAgentString)
    {
        UserAgent userAgent = new UserAgent();
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgentString);
            userAgent = new UserAgent(userAgentInfo.getOsFamily(),
                                                userAgentInfo.getOsName(),userAgentInfo.getUaFamily(),
                                                userAgentInfo.getBrowserVersionInfo(),
                                                userAgentInfo.getDeviceType(),
                                                userAgentInfo.getUaName(),
                                                userAgentInfo.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userAgent;
    }

    public static void getDevice(String userAgentString) {
    }

}


