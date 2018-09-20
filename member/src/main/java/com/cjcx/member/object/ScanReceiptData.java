package com.cjcx.member.object;

import java.util.Date;

/**
 * 扫票 自助积分 分析结果
 */
public class ScanReceiptData {

    private String serialNo;

    private Integer shopsId;
    private String shopsName;

    private String receiptNum;
    private String orderNum;


    private Double totalPrice;

    private Double cashPrice;

    private Double cardPrice;

    private Double scorePrice;

    private Double thirdPartyPrice;

    private Double wechatPrice;

    private Double alipayPrice;

    private Double otherPrice;

    private Double payPrice;



    private String payFlag;


    private Integer state;
    private Integer totalNum;

    private Date createTime;


    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getShopsId() {
        return shopsId;
    }

    public void setShopsId(Integer shopsId) {
        this.shopsId = shopsId;
    }

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }
    public Double getScorePrice() {
        return scorePrice;
    }

    public void setScorePrice(Double scorePrice) {
        this.scorePrice = scorePrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getCashPrice() {
        return cashPrice;
    }

    public void setCashPrice(Double cashPrice) {
        this.cashPrice = cashPrice;
    }

    public Double getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(Double cardPrice) {
        this.cardPrice = cardPrice;
    }

    public Double getThirdPartyPrice() {
        return thirdPartyPrice;
    }

    public void setThirdPartyPrice(Double thirdPartyPrice) {
        this.thirdPartyPrice = thirdPartyPrice;
    }

    public Double getWechatPrice() {
        return wechatPrice;
    }

    public void setWechatPrice(Double wechatPrice) {
        this.wechatPrice = wechatPrice;
    }

    public Double getAlipayPrice() {
        return alipayPrice;
    }

    public void setAlipayPrice(Double alipayPrice) {
        this.alipayPrice = alipayPrice;
    }

    public Double getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Double otherPrice) {
        this.otherPrice = otherPrice;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ScanReceiptData{" +
                "serialNo='" + serialNo + '\'' +
                ", shopsId=" + shopsId +
                ", shopsName='" + shopsName + '\'' +
                ", receiptNum='" + receiptNum + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", totalPrice=" + totalPrice +
                ", cashPrice=" + cashPrice +
                ", cardPrice=" + cardPrice +
                ", scorePrice=" + scorePrice +
                ", thirdPartyPrice=" + thirdPartyPrice +
                ", wechatPrice=" + wechatPrice +
                ", alipayPrice=" + alipayPrice +
                ", otherPrice=" + otherPrice +
                ", payPrice=" + payPrice +
                ", payFlag='" + payFlag + '\'' +
                ", state=" + state +
                ", totalNum=" + totalNum +
                ", createTime=" + createTime +
                '}';
    }
}
