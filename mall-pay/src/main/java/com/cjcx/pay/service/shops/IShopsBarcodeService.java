package com.cjcx.pay.service.shops;


import com.cjcx.pay.dto.ShopsBarcodeDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.List;

public interface IShopsBarcodeService extends IBaseService<ShopsBarcodeDto, Integer> {

    List<ShopsBarcodeDto> getShopsBarcodeList(FilterDto dto);

}
