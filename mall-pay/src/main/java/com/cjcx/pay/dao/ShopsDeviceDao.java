package com.cjcx.pay.dao;

import com.cjcx.pay.dto.ShopsDeviceDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopsDeviceDao extends IBaseDao<ShopsDeviceDto, Integer> {

    List<ShopsDeviceDto> getList(FilterDto dto);
}
