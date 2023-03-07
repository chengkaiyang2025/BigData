package com.yzf.di.newdzjavalogtohive.util;

import com.yzf.di.newdzjavalogtohive.constants.DeploymentConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 默认读取 DEFAULT_PROPERTIES_FILE 中的配置文件。
 * 再优先读取DEV_PROPERTIES_FILE,PROD_PROPERTIES_FILE中属性
 */
public class MyPropertiesUtil {
    private Properties properties;
    private InputStream defaultProperties;
    private InputStream envProperties;

    public MyPropertiesUtil(String mode) throws IOException {
        properties= new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        defaultProperties = MyPropertiesUtil.class.getClassLoader().getResourceAsStream(DeploymentConstant.DEFAULT_PROPERTIES_FILE);

        switch (mode) {
            case "prod": envProperties = MyPropertiesUtil.class.getClassLoader().getResourceAsStream(DeploymentConstant.PROD_PROPERTIES_FILE);
                        break;
            case "dev": envProperties = MyPropertiesUtil.class.getClassLoader().getResourceAsStream(DeploymentConstant.DEV_PROPERTIES_FILE);
                        break;
        }

        properties.load(defaultProperties);
        properties.load(envProperties);

    }


    public String get(String key){
        //获取key对应的value值
        return properties.getProperty(key);
    }
}
