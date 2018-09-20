package com.cjcx.pay.service;

import com.cjcx.pay.dao.SystemParamDao;
import com.cjcx.pay.dto.SystemParamDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
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
