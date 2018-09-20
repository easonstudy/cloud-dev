package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

/**
 * t_score_setting
 *
 * @author
 */
public class ScoreSetting extends BaseEntity {
    /**
     * 键
     */
    protected String name;

    /**
     * 值
     */
    protected String value;

    protected String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}