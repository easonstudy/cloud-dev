package com.cjcx.wechat.open.dto;


import com.cjcx.wechat.open.entity.UserBalance;

public class UserBalanceDto extends UserBalance {

    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
