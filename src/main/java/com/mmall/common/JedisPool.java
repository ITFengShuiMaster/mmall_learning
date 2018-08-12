package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Luyue
 * @date 2018/8/11 14:29
 **/
public class JedisPool {
    private static redis.clients.jedis.JedisPool pool;

    private static String ip = PropertiesUtil.getKey("redis.ip");
    private static Integer port = Integer.parseInt(PropertiesUtil.getKey("redis.port", "6379"));

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getKey("redis.max.total", "20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getKey("redis.max.idle", "10"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getKey("redis.min.idle", "2"));

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getKey("redis.test.borrow", "true"));
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getKey("redis.test.return", "true"));

    private static void initPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);

        //当连接池无空闲连接时，是否阻塞
        jedisPoolConfig.setBlockWhenExhausted(true);

        pool = new redis.clients.jedis.JedisPool(jedisPoolConfig, ip, port, 1000*2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenJedis(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnJedis(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("luyue", "luyue");
        returnJedis(jedis);

        pool.destroy();
        System.out.println("down*************************");
    }
}
