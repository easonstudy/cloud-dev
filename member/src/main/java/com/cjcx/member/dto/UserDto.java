package com.cjcx.member.dto;

import com.cjcx.member.entity.User;

public class UserDto extends User {

    /**
     * 用户的唯一标记 电话 openId idCard
     */
    private String userId;

    //完善资料key
    private String perfectKey;

    /**
     * 剩余积分
     */
    private String score;

    public String getPerfectKey() {
        return perfectKey;
    }

    public void setPerfectKey(String perfectKey) {
        this.perfectKey = perfectKey;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
