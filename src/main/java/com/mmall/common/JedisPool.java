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

    //redis ip 地址
    private static String ip = PropertiesUtil.getKey("redis2.ip");
    //redis 端口号
    private static Integer port = Integer.parseInt(PropertiesUtil.getKey("redis2.port", "6379"));

    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getKey("redis.max.total", "20"));
    //最大空闲连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getKey("redis.max.idle", "10"));
    //最小空闲连接数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getKey("redis.min.idle", "2"));

    //通过连接池拿去jedis连接时，校验并返回可用连接
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getKey("redis.test.borrow", "true"));
    //通过连接池返还jedis连接时，校验该连接
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getKey("redis.test.return", "true"));

    /**
     * 初始化连接池
     */
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

    /**
     * 获取一个连接
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 返还错误的连接
     * @param jedis
     */
    public static void returnBrokenJedis(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    /**
     * 返还连接
     * @param jedis
     */
    public static void returnJedis(Jedis jedis) {
        pool.returnResource(jedis);
    }
}
