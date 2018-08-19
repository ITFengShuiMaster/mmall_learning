package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Luyue
 * @date 2018/8/19 13:02
 **/
@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();
    private Redisson redisson = null;

    private static String ip1 = PropertiesUtil.getKey("redis1.ip");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getKey("redis1.port", "6379"));
    
    @PostConstruct
    /** 初始化Redisson
     *@param  []
     *@return  void
     *@author  卢越
     *@date  2018/8/19
     */
    private void init() {
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(ip1).append(":").append(port1).toString());
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson完成..........");
        } catch (Exception e) {
            log.error("初始化Redisson失败.........", e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }
}
