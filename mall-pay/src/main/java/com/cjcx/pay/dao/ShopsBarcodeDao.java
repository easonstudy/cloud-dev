package com.cjcx.pay.dao;

import com.cjcx.pay.dto.ShopsBarcodeDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopsBarcodeDao extends IBaseDao<ShopsBarcodeDto, Integer> {

    List<ShopsBarcodeDto> getList(FilterDto filterDto);


}
