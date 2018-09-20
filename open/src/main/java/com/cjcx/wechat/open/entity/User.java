package com.cjcx.wechat.open.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    protected Integer id;

    /**
     * 修改时间
     */
    protected Date updateTime;

    protected String updateBy;

    /**
     * 创建时间
     */
    protected Date createTime;

    protected String createBy;

    protected String nickName;

    protected String wechatId;

    protected String openId;

    protected String oauthOpenId;

    protected String phone;

    protected String code;

    protected String email;

    protected Boolean isDelete;

    protected UserBalance userBalance;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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