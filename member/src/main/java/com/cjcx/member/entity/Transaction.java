package com.cjcx.member.entity;


import com.cjcx.member.framework.orm.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Transaction  extends BaseEntity {

    protected Date accountDay;

    protected Integer userId;

    protected String openId;

    protected String phone;

    protected Integer type;

    protected String uniqueId;

    protected BigDecimal amount;

    protected BigDecimal score;

    protected String insertMode;

    protected Integer state;

    public Date getAccountDay() {
        return accountDay;
    }

    public void setAccountDay(Date accountDay) {
        this.accountDay = accountDay;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getInsertMode() {
        return insertMode;
    }

    public void setInsertMode(String insertMode) {
        this.insertMode = insertMode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
