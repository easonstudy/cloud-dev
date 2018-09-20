package com.cjcx.member.dao;


import com.cjcx.member.dto.PermissionsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface PermissionsDao extends IBaseDao<PermissionsDto, Integer> {

    List<PermissionsDto> getPermissionsByRoleId(Integer roleId);
}
