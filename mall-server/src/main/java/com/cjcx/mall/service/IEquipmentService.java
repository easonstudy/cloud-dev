package com.cjcx.mall.service;

import com.cjcx.mall.dto.EquipmentDto;
import com.cjcx.mall.web.ResultObject;

public interface IEquipmentService {

    ResultObject insert(EquipmentDto dto);

    ResultObject delete(EquipmentDto dto);

    ResultObject update(EquipmentDto dto);

    ResultObject select(String equipmentId);
}
