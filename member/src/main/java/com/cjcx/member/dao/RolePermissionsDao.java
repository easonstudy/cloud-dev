package com.cjcx.member.dao;


import com.cjcx.member.dto.RolePermissionsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface RolePermissionsDao extends IBaseDao<RolePermissionsDto, Integer>{

    int deleteByRoleId(Integer roleId);
}
