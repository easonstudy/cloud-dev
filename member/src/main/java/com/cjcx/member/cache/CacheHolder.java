package com.cjcx.member.cache;

import com.cjcx.member.dto.SystemParamDto;
import com.cjcx.member.service.SystemParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class CacheHolder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SystemParamService systemParamService;

    public void start(long period) {
        long s = System.currentTimeMillis();

        /** 系統参数 */
        initSystemParam();
        logger.info("Cache 结束 用时:" + (System.currentTimeMillis() - s) + "ms");
    }

    public void initSystemParam() {
        long sc = System.currentTimeMillis();
        try {
            List<SystemParamDto> list = systemParamService.findAll();
            for (int i = 0; i < list.size(); i++) {
                SystemParamDto o = list.get(i);
                systemParamMap.put(o.getParamKey(), o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("===>>> Init SystemParam Put End   <<<=== All Time:" + (System.currentTimeMillis() - sc));
        }
    }

    ConcurrentMap<String, SystemParamDto> systemParamMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, SystemParamDto> getSystemParamMap() {
        return systemParamMap;
    }

    public void setSystemParamMap(ConcurrentMap<String, SystemParamDto> systemParamMap) {
        this.systemParamMap = systemParamMap;
    }
}
