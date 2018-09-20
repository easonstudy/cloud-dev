package com.cjcx.member.entity;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.dto.UserDto;
import com.cjcx.member.framework.orm.BaseEntity;

import java.util.Date;

/**
 * t_transaction_gift
 *
 * @author
 */
public class TransactionGift extends BaseEntity {
    /**
     * 会员用户名
     */
    protected String phone;

    /**
     * 用户ID
     */
    protected Integer userId;
    /**
     * 礼品ID
     */
    protected Integer giftId;


    /**
     * 总消费积分
     */
    protected Double score;

    /**
     * 兑换数量
     */
    protected Integer number;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }




    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}