package com.mmall.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by geely
 */
@Slf4j

public class PropertiesUtil {

    private static Properties prop;

    static {
        String fileName = "mmall.properties";
        prop = new Properties();
        try {
            //加载Properties
            prop.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            log.error("初始化配置失败", e);
        }
    }

    public static String getKey(String key) {
        String value = prop.getProperty(key.trim());
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    public static String getKey(String key, String defaultValue) {
        String value = prop.getProperty(key.trim());
        if (value == null) {
            return defaultValue;
        }
        return value.trim();
    }


}
