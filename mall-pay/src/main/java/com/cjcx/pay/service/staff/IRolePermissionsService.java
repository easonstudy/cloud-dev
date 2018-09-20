package com.cjcx.pay.service.staff;


import com.cjcx.pay.dto.RolePermissionsDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.Map;

public interface IRolePermissionsService extends IBaseService<RolePermissionsDto, Integer> {

    public Map<String, Object> updateRolePermissions(Integer id, String permissions);
}
