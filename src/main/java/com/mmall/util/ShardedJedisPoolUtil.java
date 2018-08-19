package com.mmall.util;

import com.mmall.common.JedisPool;
import com.mmall.common.ShardJedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * @author Luyue
 * @date 2018/8/11 14:29
 **/
@Slf4j
public class ShardedJedisPoolUtil {

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{}, value:{} is error:{}", key, value, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static String setEx(String key, String value, int exTime) {
        ShardedJedis jedis = null;
        String response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx key:{}, value:{} is error:{}", key, value, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{}, is error:{}", key, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{}, is error:{}", key, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{}, is error:{}", key, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static Long setNx(String key, String value) {
        ShardedJedis jedis = null;
        Long response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setNx key:{}, value:{} is error:{}", key, value, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String response = null;

        try {
            jedis = ShardJedisPool.getJedis();
            response = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getSet key:{}, value:{} is error:{}", key, value, e);
            ShardJedisPool.returnBrokenJedis(jedis);
            return response;
        }

        ShardJedisPool.returnJedis(jedis);
        return response;
    }

    public static void main(String[] args) {
        Jedis jedis = JedisPool.getJedis();

        String res = ShardedJedisPoolUtil.set("testKeyNoEx", "1111");

        res = ShardedJedisPoolUtil.get("testKeyNoEx");

        res = ShardedJedisPoolUtil.setEx("testKeyEx", "22222", 60*10);

        Long resEx = ShardedJedisPoolUtil.expire("testKeyNoEx", 60*2);

        resEx = ShardedJedisPoolUtil.del("testKeyEx");
    }
}
