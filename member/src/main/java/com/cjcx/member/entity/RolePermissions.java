package com.cjcx.member.entity;


import com.cjcx.member.framework.orm.BaseEntity;

public class RolePermissions extends BaseEntity {

    protected Integer roleId;

    protected Integer permissionsId;

    protected String details;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionsId() {
        return permissionsId;
    }

    public void setPermissionsId(Integer permissionsId) {
        this.permissionsId = permissionsId;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
