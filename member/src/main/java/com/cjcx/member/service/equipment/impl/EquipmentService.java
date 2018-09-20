package com.cjcx.member.service.equipment.impl;

import com.cjcx.member.framework.utils.JedisUtils;
import com.cjcx.member.service.equipment.IEquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EquipmentService implements IEquipmentService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String EQUIPMENT_KEY = "equipment:";

    @Override
    public Map<String, Object> getEquipmentInfoById(String equipmentId) {
        Map<String, Object> outmap = new HashMap<>();

        String key = EQUIPMENT_KEY + equipmentId;
        try {
            if(JedisUtils.exists(key)){
                String str = JedisUtils.get(key);
                //字符转对象

            } else {
                //请求现有的服务器 登入方法，返回一个端口

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取设备端口 异常:{}", e.getMessage());
            outmap.put("port", "9092");
        } finally {

        }
        return null;
    }
}
