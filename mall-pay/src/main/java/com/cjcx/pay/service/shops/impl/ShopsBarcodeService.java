package com.cjcx.pay.service.shops.impl;

import com.cjcx.pay.dao.ShopsBarcodeDao;
import com.cjcx.pay.dto.ShopsBarcodeDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.shops.IShopsBarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopsBarcodeService extends BaseService<ShopsBarcodeDto, Integer> implements IShopsBarcodeService {
    @Autowired
    ShopsBarcodeDao dao;

    @Override
    protected IBaseDao<ShopsBarcodeDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<ShopsBarcodeDto> getShopsBarcodeList(FilterDto dto) {
        return dao.getList(dto);
    }





}

