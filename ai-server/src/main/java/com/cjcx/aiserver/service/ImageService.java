package com.cjcx.aiserver.service;

import com.cjcx.aiserver.ai.ExcuteInterface;
import com.cjcx.aiserver.ai.IocBeanFactory;
import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.cache.CacheHolder;
import com.cjcx.aiserver.obj.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageService implements IImageService {

    @Autowired
    CacheHolder cacheHolder;

    public ResultObject imageHandler(ImageConfig config) {
        ResultObject r = new ResultObject();
        try {
            log.info("图像处理=========================开始");
            //处理器获取
            ExcuteInterface ha = IocBeanFactory.getHandler(config.getHanderType());

            //执行
            r = ha.execute(config);

            //执行后更新次数限制

            log.info("图像处理=========================结束");
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
            r.setMsg("异常");
        }
        return r;
    }




}
