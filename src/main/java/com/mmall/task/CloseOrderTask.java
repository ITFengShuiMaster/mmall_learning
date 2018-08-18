package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
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

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeOrderTaskV1() {
        log.info("定时关单开始.........");
        int hour = Integer.parseInt(PropertiesUtil.getKey("close.order.time.task", "2"));
        iOrderService.closeOrder(hour);
        log.info("定时关单关闭.........");
    }
}
