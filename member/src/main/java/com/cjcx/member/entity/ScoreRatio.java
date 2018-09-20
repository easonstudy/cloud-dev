package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

import java.util.Date;

/**
 * t_score_ratio
 *
 * @author
 */
public class ScoreRatio extends BaseEntity {
    /**
     * 名称
     */
    protected String name;

    /**
     * 兑换比列
     */
    protected Double ratio;

    protected boolean isDelete;

    protected String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}