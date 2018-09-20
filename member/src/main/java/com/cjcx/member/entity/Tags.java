package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;

/**
 * t_tags
 *
 * @author
 */
public class Tags extends BaseEntity {
    /**
     * 标签类型
     */
    protected String type;

    /**
     * 值
     */
    protected String name;

    protected static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}