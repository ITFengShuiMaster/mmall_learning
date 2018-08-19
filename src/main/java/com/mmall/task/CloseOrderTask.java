package com.mmall.task;

import com.mmall.common.Constants;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/** 定时任务（关闭未支付的订单）
 * @author Luyue
 * @date 2018/8/18 14:40
 **/
@Component
@Slf4j
public class CloseOrderTask {

    private final IOrderService iOrderService;
    private final RedissonManager redissonManager;

    @Autowired
    public CloseOrderTask(IOrderService iOrderService, RedissonManager redissonManager) {
        this.iOrderService = iOrderService;
        this.redissonManager = redissonManager;
    }

    /**
     * 无分布式锁
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV1() {
        log.info("定时关单开始.........");
        int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
//        iOrderService.closeOrder(hour);
        log.info("定时关单关闭.........");
    }

    /**
     * 初代分布式锁
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV2() {
        log.info("定时关单开始.........");
        Long lockTime = Long.parseLong(PropertiesUtil.getKey("lock.time", "5000"));
        Long setNxResult = ShardedJedisPoolUtil.setNx(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY, String.valueOf(System.currentTimeMillis()+lockTime));

        if (setNxResult != null && setNxResult.intValue() == 1) {
            log.info("获得分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
            closeOrder(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
        } else {
            log.info("未获得分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
        }
        log.info("定时关单关闭.........");
    }

    /**
     * 演进分布式锁，防死锁
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV3() {
        log.info("定时关单开始.........");
        Long lockTime = Long.parseLong(PropertiesUtil.getKey("lock.time", "5000"));
        Long setNxResult = ShardedJedisPoolUtil.setNx(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY, String.valueOf(System.currentTimeMillis()+lockTime));

        if (setNxResult != null && setNxResult.intValue() == 1) {
            log.info("获得分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
            closeOrder(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
        } else {
            String lockValue = ShardedJedisPoolUtil.get(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
            if (lockValue != null && System.currentTimeMillis() > Long.parseLong(lockValue)) {
                String getSetResult = ShardedJedisPoolUtil.getSet(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY, String.valueOf(System.currentTimeMillis()+lockTime));
                //1. 如果getSetResult为null，表示key没有旧值
                //2. 将getSetResult和lockValue比较，相等则执行关单，否则表示有其他进程执行完定时任务
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValue, getSetResult))) {
                    closeOrder(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
                } else {
                    log.info("未获得分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
                }
            } else {
                log.info("未获得分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
            }
        }
        log.info("定时关单关闭.........");
    }

    /**
     * redisson 构建分布式锁
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV4() {
        RLock lock = redissonManager.getRedisson().getLock(Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
        boolean getLock = false;
        try {
            //尝试获取分布式锁，设置等待时间，锁的释放时间，时间单位
//            if (getLock = lock.tryLock(2, 5, TimeUnit.SECONDS)) {
            if (getLock = lock.tryLock(0, 50, TimeUnit.SECONDS)) {
                log.info("获取到分布式锁: {}, Thread: {}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY, Thread.currentThread().getName());
                int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
//              iOrderService.closeOrder(hour);
            } else {
                log.info("没有获取到分布式锁: {}, Thread: {}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("redisson获取分布式锁异常", e);
        } finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.info("释放分布式锁：{}", Constants.RedisLock.CLOSE_ORDER_LOCK_KEY);
        }
    }

    /**
     * 定时任务所要执行的业务
     * @param lockName
     */
    private void closeOrder(String lockName) {
        log.info("获取{}, ThreadName:{}", lockName, Thread.currentThread().getName());
        //设置有效期，防止死锁
        ShardedJedisPoolUtil.expire(lockName, 5000);
        int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
//        iOrderService.closeOrder(hour);
        ShardedJedisPoolUtil.del(lockName);
        log.info("释放：{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        log.info("====================================");
    }
}
