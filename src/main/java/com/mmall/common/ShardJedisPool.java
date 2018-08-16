package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.List;

/**
 * @author Luyue
 * @date 2018/8/14 9:29
 **/
public class ShardJedisPool {
    private static ShardedJedisPool pool;

    private static String ip1 = PropertiesUtil.getKey("redis1.ip");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getKey("redis1.port", "6379"));

    private static String ip2 = PropertiesUtil.getKey("redis2.ip");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getKey("redis2.port", "6380"));

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

        JedisShardInfo info1 = new JedisShardInfo(ip1, port1, 1000 * 20);
        JedisShardInfo info2 = new JedisShardInfo(ip2, port2, 1000 * 20);

        List<JedisShardInfo> infoList = Lists.newArrayList(info1, info2);

        pool = new ShardedJedisPool(jedisPoolConfig, infoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenJedis(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnJedis(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = getJedis();

        for(int i = 0 ; i < 10 ; i ++) {
            jedis.set("key"+i, "value"+i);
        }

        returnJedis(jedis);
        System.out.println("down*************************");
    }
}
