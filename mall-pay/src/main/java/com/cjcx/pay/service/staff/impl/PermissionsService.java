package com.cjcx.pay.service.staff.impl;

import com.cjcx.pay.dao.PermissionsDao;
import com.cjcx.pay.dto.PermissionsDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.staff.IPermissionsService;
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
