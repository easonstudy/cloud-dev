package com.cjcx.member.service.score.impl;

import com.cjcx.member.dao.GiftDao;
import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.score.IGiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftService extends BaseService<GiftDto, Integer> implements IGiftService {

    @Autowired
    GiftDao dao;

    @Override
    protected IBaseDao<GiftDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<GiftDto> searchByCondition(GiftDto dto) {
        try {
            return dao.searchByCondition(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
