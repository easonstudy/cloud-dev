package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_rule_tags
 * @author 
 */
public class UserRuleTags extends BaseEntity implements Serializable {

    private Integer tagsId;

    private Integer levelLow;

    private Integer levelMiddle;

    private Integer levelHigh;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagsId() {
        return tagsId;
    }
    public void setTagsId(Integer tagsId) {
        this.tagsId = tagsId;
    }

    public Integer getLevelLow() {
        return levelLow;
    }

    public void setLevelLow(Integer levelLow) {
        this.levelLow = levelLow;
    }

    public Integer getLevelMiddle() {
        return levelMiddle;
    }

    public void setLevelMiddle(Integer levelMiddle) {
        this.levelMiddle = levelMiddle;
    }

    public Integer getLevelHigh() {
        return levelHigh;
    }

    public void setLevelHigh(Integer levelHigh) {
        this.levelHigh = levelHigh;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserRuleTags other = (UserRuleTags) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTagsId() == null ? other.getTagsId() == null : this.getTagsId().equals(other.getTagsId()))
            && (this.getLevelLow() == null ? other.getLevelLow() == null : this.getLevelLow().equals(other.getLevelLow()))
            && (this.getLevelMiddle() == null ? other.getLevelMiddle() == null : this.getLevelMiddle().equals(other.getLevelMiddle()))
            && (this.getLevelHigh() == null ? other.getLevelHigh() == null : this.getLevelHigh().equals(other.getLevelHigh()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()));
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tagsId=").append(tagsId);
        sb.append(", levelLow=").append(levelLow);
        sb.append(", levelMiddle=").append(levelMiddle);
        sb.append(", levelHigh=").append(levelHigh);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}