package com.cjcx.aiserver.service;

import com.cjcx.aiserver.ai.ExcuteInterface;
import com.cjcx.aiserver.ai.IocBeanFactory;
import com.cjcx.aiserver.ai.config.FaceConfig;
import com.cjcx.aiserver.obj.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FaceService implements IFaceService {

    @Override
    public ResultObject faceHandler(FaceConfig config) {
        ResultObject r = new ResultObject();
        try {
            log.info("人脸处理=========================开始");
            //处理器获取
            ExcuteInterface ha = IocBeanFactory.getHandler(config.getHanderType());

            //执行
            r = ha.execute(config);

            log.info("人脸处理=========================结束");
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
            r.setMsg("异常");
        }
        return r;
    }
}
