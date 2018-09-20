package com.cjcx.wechat.open.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * Model 基类，所有Model类应该继承于此类
 */
public abstract class BaseEntity implements Serializable {

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
