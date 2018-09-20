package com.cjcx.member.dto;

import com.cjcx.member.entity.UserBalance;

public class UserBalanceDto extends UserBalance {

    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
