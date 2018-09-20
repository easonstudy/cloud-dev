package com.cjcx.member.service.staff;

import com.cjcx.member.dto.PermissionsDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.List;

public interface IPermissionsService extends IBaseService<PermissionsDto, Integer> {

    List<PermissionsDto> getPermissionsByRoleId(Integer roleId);
}
