package com.cjcx.mall.service;

import com.cjcx.mall.dto.EquipmentDto;
import com.cjcx.mall.web.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService implements IEquipmentService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public ResultObject insert(EquipmentDto dto) {
        ResultObject r = new ResultObject();

        System.out.println(dto.toString());
        redisTemplate.opsForValue().set(dto.getEquipmentId(), dto.getRequestAddress());
        return r;
    }

    @Override
    public ResultObject delete(EquipmentDto dto) {
        ResultObject r = new ResultObject();

        System.out.println(dto.toString());
        redisTemplate.delete(dto.getEquipmentId());
        return r;
    }

    @Override
    public ResultObject update(EquipmentDto dto) {
        ResultObject r = new ResultObject();

        System.out.println(dto.toString());
        redisTemplate.opsForValue().set(dto.getEquipmentId(), dto.getRequestAddress());
        return r;
    }

    @Override
    public ResultObject select(String  equipmentId) {
        ResultObject r = new ResultObject();
        System.out.println("select :" + equipmentId);
        String v = (String) redisTemplate.opsForValue().get(equipmentId);
        r.setData(v);
        return r;
    }
}
