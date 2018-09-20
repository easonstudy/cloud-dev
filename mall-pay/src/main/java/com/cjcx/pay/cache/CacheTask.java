package com.cjcx.pay.cache;

import com.cjcx.pay.dto.SystemParamDto;
import com.cjcx.pay.service.SystemParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时任务
 */
@Component
public class CacheTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CacheHolder cacheHolder;

    @Autowired
    SystemParamService systemParamService;

    /**
     * 系统参数 5分钟一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void systemParam() {

        long s = System.currentTimeMillis();
        logger.debug("任务: " + Thread.currentThread().getName() + " SystemParam 开始");
        try {
            ConcurrentMap<String, SystemParamDto> systemParamMap = cacheHolder.getSystemParamMap();
            List<SystemParamDto> list = systemParamService.findAll();
            for (int i = 0; i < list.size(); i++) {
                SystemParamDto o = list.get(i);
                systemParamMap.put(o.getParamKey(), o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("任务: " + Thread.currentThread().getName() + " SystemParam 结束 用时:" + (System.currentTimeMillis() - s) + "ms");
    }

}
