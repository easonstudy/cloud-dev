package com.cjcx.member.service.staff.impl;

import com.cjcx.member.dao.RoleDao;
import com.cjcx.member.dto.RoleDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.staff.IRoleService;
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
