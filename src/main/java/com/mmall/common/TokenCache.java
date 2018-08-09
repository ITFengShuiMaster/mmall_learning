package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Luyue
 * @date 2018/7/30 12:10
 **/
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
            //初始化缓存大小
            .initialCapacity(1000)
            //最大缓存大小
            .maximumSize(10000)
            //缓存过期时间
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                //默认的数据加载实现，如果在调用get方法时key没有对应的值，就调用该方法加载
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        loadingCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = loadingCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("获取key失败", e);
        }
        return null;
    }
}
