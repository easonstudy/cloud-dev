package com.cjcx.pay.service.staff.impl;


import com.cjcx.pay.dao.RoleDao;
import com.cjcx.pay.dto.RoleDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.staff.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService  extends BaseService<RoleDto, Integer> implements IRoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    protected IBaseDao<RoleDto, Integer> getDao() {
        return roleDao;
    }
}
