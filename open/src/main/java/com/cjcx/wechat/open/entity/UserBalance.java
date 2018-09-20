package com.cjcx.wechat.open.entity;


import java.math.BigDecimal;

public class UserBalance extends BaseEntity {

    private Integer userId;

    private BigDecimal score;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}