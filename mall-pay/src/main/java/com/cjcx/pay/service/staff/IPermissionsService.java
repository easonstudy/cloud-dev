package com.cjcx.pay.service.staff;


import com.cjcx.pay.dto.PermissionsDto;
import com.cjcx.pay.framework.service.IBaseService;

import java.util.List;

public interface IPermissionsService extends IBaseService<PermissionsDto, Integer> {

    List<PermissionsDto> getPermissionsByRoleId(Integer roleId);
}
