package com.cjcx.member.dto;

import com.cjcx.member.entity.UserRequest;

public class UserRequestDto extends UserRequest {

    private String userKey;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
