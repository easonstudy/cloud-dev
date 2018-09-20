package com.cjcx.member.entity;


import com.cjcx.member.framework.orm.BaseEntity;

import java.math.BigDecimal;

public class UserBalance extends BaseEntity {

    protected Integer userId;

    protected Double score;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
