package com.yzf.config;

import java.io.IOException;
import java.util.Properties;

/**
 * @author ：yangchengkai@yunzhangfang.com
 * @description：从配置文件config-dev.properties中获取数据
 * @date ：2021/1/18 下午2:46
 */


public class Configuration {
    public static String NGINX_TOPIC;

    public static Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(Configuration.class.getClassLoader().getResourceAsStream("config-prod.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        NGINX_TOPIC = PROPERTIES.getProperty("nginx_log_anticrawler.topic");
//        DDLBINLOGSOURCE_TOPIC = p.getProperty("dingdinglogsource.bootstrap.servers");
//        DDLBINLOGSOURCE_ZOOKEEPER = p.getProperty("dingdinglogsource.zookeeper.connect");
//        DDLBINLOGSOURCE_BOOTSTRAP = p.getProperty("dingdinglogsource.group.id");
//        DDLBINLOGSOURCE_GROUPID = p.getProperty("dingdinglogsource.topic");

    }
}
