package com.cjcx.member.dto.filter;

import java.util.Date;

/**
 * 列表 查询过滤 公共dto
 */
public class FilterDto {

    /**
     * 用户唯一标识
     */
    private String userUniqueId;

    /**
     * state: 0未审核 2:待审核 1:审核通过 3审核拒绝
     * 小票类型
     * 0 所有记录  即全部
     * 2:历史记录  即state 不为0和1
     */
    private Integer ticketType;
    /**
     * 会员id
     */
    private Integer userId;

    private String giftCode;
    private String giftName;
    private Date giftStartTime;
    private Date giftEndTime;

    private String shopsName;
    /**
     * 会员电话
     */
    private String phone;


    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
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

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getGiftStartTime() {
        return giftStartTime;
    }

    public void setGiftStartTime(Date giftStartTime) {
        this.giftStartTime = giftStartTime;
    }

    public Date getGiftEndTime() {
        return giftEndTime;
    }

    public void setGiftEndTime(Date giftEndTime) {
        this.giftEndTime = giftEndTime;
    }
}
