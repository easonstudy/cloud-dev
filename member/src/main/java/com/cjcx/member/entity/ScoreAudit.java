package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_score_audit
 * @author 
 */
public class ScoreAudit extends BaseEntity {

    protected String phone;

    /**
     * 用户ID
     */
    protected Integer userId;

    /**
     * 小票图片地址
     */
    protected String url;

    /**
     * 管理员Id
     */
    protected Integer staffId;
    /**
     * 店铺Id
     */
    protected Integer shopsId;

    /**
     * 审核通过后的 兑换积分交易ID
     */
    protected Integer trId;

    /**
     * 用户审核状态 0未审核 2:待审核 1:审核通过 3审核拒绝
     */
    protected Integer state;

    protected String userRemark;
    protected String staffRemark;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getShopsId() {
        return shopsId;
    }

    public void setShopsId(Integer shopsId) {
        this.shopsId = shopsId;
    }

    public Integer getTrId() {
        return trId;
    }

    public void setTrId(Integer trId) {
        this.trId = trId;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getStaffRemark() {
        return staffRemark;
    }

    public void setStaffRemark(String staffRemark) {
        this.staffRemark = staffRemark;
    }
}