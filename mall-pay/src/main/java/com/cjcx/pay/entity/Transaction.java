package com.cjcx.pay.entity;

import com.cjcx.pay.framework.orm.BaseEntity;

import java.util.Date;

public class Transaction  extends BaseEntity {

    protected Date accountDay;

    protected Integer shopsId;

    protected String deviceId;

    protected String barcode;


    protected Double amount = 0.0;

    protected Double discount = 0.0;

    protected Integer type = 0;

    // 0待处理   1成功   2失败
    protected Integer state = 0;

    protected String remark;

    protected Date grasptingTime;

    public Date getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(Date accountDay) {
        this.accountDay = accountDay;
    }

    public Integer getShopsId() {
        return shopsId;
    }

    public void setShopsId(Integer shopsId) {
        this.shopsId = shopsId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getGrasptingTime() {
        return grasptingTime;
    }

    public void setGrasptingTime(Date grasptingTime) {
        this.grasptingTime = grasptingTime;
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
        return "Transaction{" +
                "accountDay=" + accountDay +
                ", shopsId=" + shopsId +
                ", deviceId='" + deviceId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", amount=" + amount +
                ", discount=" + discount +
                ", type=" + type +
                ", state=" + state +
                ", grasptingTime=" + grasptingTime +
                ", id=" + id +
                ", updateTime=" + updateTime +
                ", updateBy='" + updateBy + '\'' +
                ", createTime=" + createTime +
                ", createBy='" + createBy + '\'' +
                '}';
    }
}
