package com.mmall.util;

import com.mmall.common.JedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author Luyue
 * @date 2018/8/11 14:29
 **/
@Slf4j
public class JedisPoolUtil {

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

    public static void main(String[] args) {
        Jedis jedis = JedisPool.getJedis();

        String res = JedisPoolUtil.set("testKeyNoEx", "1111");

        res = JedisPoolUtil.get("testKeyNoEx");

        res = JedisPoolUtil.setEx("testKeyEx", "22222", 60*10);

        Long resEx = JedisPoolUtil.expire("testKeyNoEx", 60*2);

        resEx = JedisPoolUtil.del("testKeyEx");
    }
}
