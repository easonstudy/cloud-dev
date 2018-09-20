package com.cjcx.member.service.staff.impl;

import com.cjcx.member.dao.PermissionsDao;
import com.cjcx.member.dto.PermissionsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.staff.IPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsService extends BaseService<PermissionsDto, Integer> implements IPermissionsService {

    @Autowired
    PermissionsDao permissonsDao;

    @Override
    protected IBaseDao<PermissionsDto, Integer> getDao() {
        return permissonsDao;
    }

    @Override
    public List<PermissionsDto> getPermissionsByRoleId(Integer roleId) {
        try {
            return permissonsDao.getPermissionsByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
