package com.cjcx.member.entity;



import com.cjcx.member.framework.orm.BaseEntity;

import java.util.Date;

public class User extends BaseEntity {

    protected String nickName;

    protected String wechatId;

    protected String openId;

    protected String oauthOpenId;

    protected String phone;

    protected String code;

    protected String email;

    protected Boolean isDelete;

    protected UserBalance userBalance;

    public Date getCodeExpireTime() {
        return codeExpireTime;
    }

    public void setCodeExpireTime(Date codeExpireTime) {
        this.codeExpireTime = codeExpireTime;
    }

    /**
     * 创建时间
     */
    protected Date codeExpireTime;

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserBalance getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(UserBalance userBalance) {
        this.userBalance = userBalance;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getOauthOpenId() {
        return oauthOpenId;
    }

    public void setOauthOpenId(String oauthOpenId) {
        this.oauthOpenId = oauthOpenId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", openId='" + openId + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", isDelete=" + isDelete +
                ", createTime=" + createTime +
                ", userBalance=" + userBalance +
                '}';
    }
}