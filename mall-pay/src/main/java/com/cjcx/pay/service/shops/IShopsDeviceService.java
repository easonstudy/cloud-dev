package com.cjcx.pay.service.shops;


import com.cjcx.pay.dto.ShopsDeviceDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.List;

public interface IShopsDeviceService  extends IBaseService<ShopsDeviceDto, Integer> {

    List<ShopsDeviceDto> getShopsDeviceList(FilterDto dto);
}
