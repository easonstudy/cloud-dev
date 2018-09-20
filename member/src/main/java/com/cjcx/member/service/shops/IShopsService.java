package com.cjcx.member.service.shops;

import com.cjcx.member.dto.ShopsDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.List;

public interface IShopsService extends IBaseService<ShopsDto, Integer> {

    List<ShopsDto> getShopsList(FilterDto dto);
}
