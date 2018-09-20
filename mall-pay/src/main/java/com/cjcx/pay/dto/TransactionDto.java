package com.cjcx.pay.dto;


import com.cjcx.pay.entity.Transaction;

public class TransactionDto extends Transaction {

    private String shopsName;
    private String goodsName;

    public String getShopsName() {
        return shopsName;
    }
    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "accountDay=" + accountDay +
                ", shopsId=" + shopsId +
                ", shopsName=" + shopsName +
                ", goodsName=" + goodsName +
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
