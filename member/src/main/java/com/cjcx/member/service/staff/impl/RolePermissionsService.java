package com.cjcx.member.service.staff.impl;

import com.cjcx.member.dao.RolePermissionsDao;
import com.cjcx.member.dto.RolePermissionsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.staff.IRolePermissionsService;
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
