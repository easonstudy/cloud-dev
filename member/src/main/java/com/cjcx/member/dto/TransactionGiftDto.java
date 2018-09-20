package com.cjcx.member.dto;

import com.cjcx.member.entity.TransactionGift;

public class TransactionGiftDto extends TransactionGift {

    protected UserDto user;
    protected GiftDto gift;

    //会员资料
    private String nickName;
    private String openId;
    private String phone;

    //礼品资料
    private Integer giftType;
    private String giftCode;
    private String giftName;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public GiftDto getGift() {
        return gift;
    }

    public void setGift(GiftDto gift) {
        this.gift = gift;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGiftType() {
        return giftType;
    }

    public void setGiftType(Integer giftType) {
        this.giftType = giftType;
    }

    public String getGiftCode() {
        return giftCode;
    }

    public void setGiftCode(String giftCode) {
        this.giftCode = giftCode;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }
}
