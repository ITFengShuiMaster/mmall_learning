package com.mmall.util;

import com.mmall.common.JedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/** 单机redis连接池
 * @author Luyue
 * @date 2018/8/11 14:29
 **/
@Slf4j
public class JedisPoolUtil {

    /**
     * 存储键值
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String response = null;

        try {
            jedis = JedisPool.getJedis();
            response = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{}, value:{} is error:{}", key, value, e);
            JedisPool.returnBrokenJedis(jedis);
            return response;
        }

        JedisPool.returnJedis(jedis);
        return response;
    }

    /**
     * 存储键值，并设置有效时间
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String response = null;

        try {
            jedis = JedisPool.getJedis();
            response = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx key:{}, value:{} is error:{}", key, value, e);
            JedisPool.returnBrokenJedis(jedis);
            return response;
        }

        JedisPool.returnJedis(jedis);
        return response;
    }

    /**
     * 获取value
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        String response = null;

        try {
            jedis = JedisPool.getJedis();
            response = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{}, is error:{}", key, e);
            JedisPool.returnBrokenJedis(jedis);
            return response;
        }

        JedisPool.returnJedis(jedis);
        return response;
    }

    /**
     * 设置键的有效时间
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long response = null;

        try {
            jedis = JedisPool.getJedis();
            response = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{}, is error:{}", key, e);
            JedisPool.returnBrokenJedis(jedis);
            return response;
        }

        JedisPool.returnJedis(jedis);
        return response;
    }

    /**
     * 删除键
     * @param key
     * @return
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long response = null;

        try {
            jedis = JedisPool.getJedis();
            response = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{}, is error:{}", key, e);
            JedisPool.returnBrokenJedis(jedis);
            return response;
        }

        JedisPool.returnJedis(jedis);
        return response;
    }
}
