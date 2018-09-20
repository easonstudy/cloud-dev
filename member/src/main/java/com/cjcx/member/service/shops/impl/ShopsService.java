package com.cjcx.member.service.shops.impl;

import com.cjcx.member.dao.ShopsDao;
import com.cjcx.member.dto.ShopsDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.shops.IShopsService;
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
