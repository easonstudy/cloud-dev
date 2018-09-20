package com.cjcx.pay.service.shops.impl;

import com.cjcx.pay.dao.ShopsDeviceDao;
import com.cjcx.pay.dto.ShopsDeviceDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.shops.IShopsDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopsDeviceService extends BaseService<ShopsDeviceDto, Integer> implements IShopsDeviceService {

    @Autowired
    ShopsDeviceDao dao;

    @Override
    protected IBaseDao<ShopsDeviceDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<ShopsDeviceDto> getShopsDeviceList(FilterDto dto) {
        return dao.getList(dto);
    }
}

