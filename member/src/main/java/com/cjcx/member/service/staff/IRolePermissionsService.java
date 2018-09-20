package com.cjcx.member.service.staff;

import com.cjcx.member.dto.RolePermissionsDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.Map;

public interface IRolePermissionsService extends IBaseService<RolePermissionsDto, Integer> {

    public Map<String, Object> updateRolePermissions(Integer id, String permissions);
}
