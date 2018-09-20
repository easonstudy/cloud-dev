package com.cjcx.member.entity;


import com.cjcx.member.framework.orm.BaseEntity;

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

    /**
     * 店铺积分兑换比列
     */
    protected Integer scoreRatioId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScoreRatioId() {
        return scoreRatioId;
    }

    public void setScoreRatioId(Integer scoreRatioId) {
        this.scoreRatioId = scoreRatioId;
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

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

}