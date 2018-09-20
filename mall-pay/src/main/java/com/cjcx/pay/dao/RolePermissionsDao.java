package com.cjcx.pay.dao;


import com.cjcx.pay.dto.RolePermissionsDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface RolePermissionsDao extends IBaseDao<RolePermissionsDto, Integer> {

    int deleteByRoleId(Integer roleId);
}
