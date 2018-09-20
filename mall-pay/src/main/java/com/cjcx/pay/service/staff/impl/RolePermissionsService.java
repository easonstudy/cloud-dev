package com.cjcx.pay.service.staff.impl;


import com.cjcx.pay.dao.RolePermissionsDao;
import com.cjcx.pay.dto.RolePermissionsDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.service.staff.IRolePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RolePermissionsService extends BaseService<RolePermissionsDto, Integer> implements IRolePermissionsService {

    @Autowired
    RolePermissionsDao rolePermissionsDao;

    @Override
    protected IBaseDao<RolePermissionsDto, Integer> getDao() {
        return rolePermissionsDao;
    }


    @Override
    public Map<String, Object> updateRolePermissions(Integer id, String permissions) {
        Map<String, Object> outmap = new HashMap<>();

        //移除id的所有权限
        rolePermissionsDao.deleteByRoleId(id);

        String[] arr = permissions.split(",");
        for (int i = 0; i < arr.length; i++) {
            RolePermissionsDto rolePermissionsDto = new RolePermissionsDto();
            rolePermissionsDto.setRoleId(id);
            rolePermissionsDto.setPermissionsId(Integer.parseInt(arr[i]));
            rolePermissionsDao.insert(rolePermissionsDto);
        }
        return outmap;
    }
}
