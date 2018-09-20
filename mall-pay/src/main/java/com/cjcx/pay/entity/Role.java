package com.cjcx.pay.entity;


import com.cjcx.pay.framework.orm.BaseEntity;

public class Role extends BaseEntity {

    protected String name;

    protected String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
