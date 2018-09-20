package com.cjcx.wechat.open.object;

public class ReceiptData {
	private String receiptId;
	
	private String equipmentId;
	
	private Integer shopsId;
	
	private String shopName;
	
	private String receiptNum;
	
	
	private Integer totalNum;
	
	private Double totalPrice;
	
	private Double cashPrice;
	
	private Double cardPrice;
	
	private Double thirdPartyPrice;
	
	private Double otherPrice;
	
	private String payFlag;
	
	private String creatTime;
	
	private String graspingTime;


	private String imageurl;
	/**
	 * 1重打 0 正常
	 */
	private Integer isRepeat;

	/**
	 * 1退款  0 正常
	 */
	private Integer receiptStatus;


	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Integer getShopsId() {
		return shopsId;
	}

	public void setShopsId(Integer shopsId) {
		this.shopsId = shopsId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(Integer receiptStatus) {
		this.receiptStatus = receiptStatus;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
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

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getGraspingTime() {
		return graspingTime;
	}

	public void setGraspingTime(String graspingTime) {
		this.graspingTime = graspingTime;
	}

	public Integer getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(Integer isRepeat) {
		this.isRepeat = isRepeat;
	}

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "ReceiptData{" +
                "receiptId='" + receiptId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", shopsId=" + shopsId +
                ", shopName='" + shopName + '\'' +
                ", receiptNum='" + receiptNum + '\'' +
                ", totalNum=" + totalNum +
                ", totalPrice=" + totalPrice +
                ", cashPrice=" + cashPrice +
                ", cardPrice=" + cardPrice +
                ", thirdPartyPrice=" + thirdPartyPrice +
                ", otherPrice=" + otherPrice +
                ", payFlag='" + payFlag + '\'' +
                ", creatTime='" + creatTime + '\'' +
                ", graspingTime='" + graspingTime + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", isRepeat=" + isRepeat +
                ", receiptStatus=" + receiptStatus +
                '}';
    }
}
