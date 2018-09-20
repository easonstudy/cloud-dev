package com.cjcx.pay.dao;

import com.cjcx.pay.dto.ShopsDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopsDao extends IBaseDao<ShopsDto, Integer> {

    List<ShopsDto> getShopsList(FilterDto filterDto);


}
