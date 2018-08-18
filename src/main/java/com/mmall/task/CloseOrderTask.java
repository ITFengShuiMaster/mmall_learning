package com.mmall.task;

import com.mmall.common.Constants;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.ShardedJedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Luyue
 * @date 2018/8/18 14:40
 **/
@Component
@Slf4j
public class CloseOrderTask {

    private final IOrderService iOrderService;

    @Autowired
    public CloseOrderTask(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV1() {
        log.info("定时关单开始.........");
        int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
//        iOrderService.closeOrder(hour);
        log.info("定时关单关闭.........");
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
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

    private void closeOrder(String lockName) {
        log.info("获取{}, ThreadName:{}", lockName, Thread.currentThread().getName());
        //设置有效期，防止死锁
        ShardedJedisPoolUtil.expire(lockName, 5000);
        int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
        iOrderService.closeOrder(hour);
        ShardedJedisPoolUtil.del(lockName);
        log.info("释放：{}， ThreadName：{}", lockName, Thread.currentThread().getName());
        log.info("====================================");
    }
}
