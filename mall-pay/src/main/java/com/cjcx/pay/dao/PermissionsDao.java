package com.cjcx.pay.dao;


import com.cjcx.pay.dto.PermissionsDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface PermissionsDao extends IBaseDao<PermissionsDto, Integer> {

    List<PermissionsDto> getPermissionsByRoleId(Integer roleId);
}
