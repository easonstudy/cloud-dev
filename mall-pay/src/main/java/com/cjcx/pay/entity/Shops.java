package com.cjcx.pay.entity;


import com.cjcx.pay.framework.orm.BaseEntity;

/**
 * t_shops
 *
 * @author
 */
public class Shops extends BaseEntity {
    /**
     * 店铺名
     */
    protected String name;
    /**
     * 店铺电话
     */
    protected String phone;
    /**
     * 店铺地址
     */
    protected String address;


    protected boolean isDelete;

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}