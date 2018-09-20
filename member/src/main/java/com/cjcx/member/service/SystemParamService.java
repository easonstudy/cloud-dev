package com.cjcx.member.service;

import com.cjcx.member.dao.SystemParamDao;
import com.cjcx.member.dto.SystemParamDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.ISystemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemParamService extends BaseService<SystemParamDto, Integer> implements ISystemParamService {


    @Autowired
    SystemParamDao systemParamDao;

    @Override
    protected IBaseDao<SystemParamDto, Integer> getDao() {
        return systemParamDao;
    }
}
