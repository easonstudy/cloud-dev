package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

/**
 * t_user_level
 *
 * @author
 */
public class UserLevel extends BaseEntity {

    protected String name;

    protected Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}