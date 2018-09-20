package com.cjcx.pay.service.shops;


import com.cjcx.pay.dto.ShopsDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.List;

public interface IShopsService extends IBaseService<ShopsDto, Integer> {

    List<ShopsDto> getShopsList(FilterDto dto);
}
