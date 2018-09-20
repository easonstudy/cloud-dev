package com.cjcx.pay.entity;

import com.cjcx.pay.framework.orm.BaseEntity;

/**
 * t_shops_barcode
 * @author 
 */
public class ShopsBarcode extends BaseEntity {

    protected Integer shopsId;

    /**
     * 条码商品名
     */
    protected String name;

    protected String barcode;

    protected Double amount;

    protected String wechatCode;

    protected Double discount;

    protected String remark;

    protected boolean isDelete;

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getShopsId() {
        return shopsId;
    }

    public void setShopsId(Integer shopsId) {
        this.shopsId = shopsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechatCode() {
        return wechatCode;
    }

    public void setWechatCode(String wechatCode) {
        this.wechatCode = wechatCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ShopsBarcode{" +
                "shopsId=" + shopsId +
                ", name='" + name + '\'' +
                ", wechatCode='" + wechatCode + '\'' +
                ", barcode='" + barcode + '\'' +
                ", amount=" + amount +
                ", isDelete=" + isDelete +
                ", id=" + id +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                '}';
    }
}