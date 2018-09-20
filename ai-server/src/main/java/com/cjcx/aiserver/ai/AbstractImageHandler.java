package com.cjcx.aiserver.ai;

import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.cache.CacheHolder;
import com.cjcx.aiserver.obj.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * AI识别公共类
 */
@Slf4j
public abstract class AbstractImageHandler implements ExcuteInterface {

    @Autowired
    CacheHolder cacheHolder;

    //配置
    protected ImageConfig imageConfig;

    //高精度&位置容错值(top)
    protected Integer FAULT_TOLERANT_VALUE = 5;

    protected abstract void afterExecute(String content, ResultObject r);

    protected void limit(String key, String request_address) {
        //request_address的限制次数
        AbstractAccountManager manager = IocBeanFactory.getAccountManager(imageConfig.getAction());
        Integer timesMax = manager.getAccountLimitByKey(key).get(request_address);

        ConcurrentMap<String, ConcurrentMap<String, Integer>> limitTimesMap = cacheHolder.getLimitTimesMap();
        ConcurrentMap<String, Integer> limitMap = limitTimesMap.get(key);

        if (limitMap == null) {
            //第一次
            log.info("首次计次 apiKey:{}, timesNow:{}, timesMax:{}", key, 1, timesMax);
            ConcurrentMap<String, Integer> timesMap = new ConcurrentHashMap<>();
            timesMap.put(request_address, 1);
            limitTimesMap.put(key, timesMap);
            return;
        }

        //已调用request_address的次数
        Integer times = limitMap.get(request_address);
        times = times == null ? 1 : times + 1;
        log.info("apiKey:{}, timesNow:{}, timesMax:{}", key, times, timesMax);

        if (times <= timesMax) {
            //若单账户的限制
            limitMap.put(request_address, times);
            limitTimesMap.put(key, limitMap);
            if (times == timesMax) {
                //切换账户 停用该账户
                log.info("已执行次数:{}, 限制次数:{}", times, timesMax);
                manager.stopAccountByKey(key);
            }
        } else if (times > timesMax) {
            //正常不会执行到这里, 停用账户
            manager.stopAccountByKey(key);
        }
    }

    public ImageConfig getImageConfig() {
        return imageConfig;
    }

    public void setImageConfig(ImageConfig imageConfig) {
        this.imageConfig = imageConfig;
    }
}
