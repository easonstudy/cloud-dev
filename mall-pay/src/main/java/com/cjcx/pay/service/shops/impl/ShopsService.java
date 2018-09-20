package com.cjcx.pay.service.shops.impl;

import com.cjcx.pay.dao.ShopsDao;
import com.cjcx.pay.dto.ShopsDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.shops.IShopsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopsService extends BaseService<ShopsDto, Integer> implements IShopsService {

    @Autowired
    ShopsDao dao;

    @Override
    protected IBaseDao<ShopsDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<ShopsDto> getShopsList(FilterDto dto) {
        try {
            return dao.getShopsList(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
