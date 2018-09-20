package com.cjcx.member.dto;

import com.cjcx.member.entity.Gift;

public class GiftDto extends Gift {

    /**
     * 用户唯一标识
     */
    private String userUniqueId;

    /**
     * 查找礼品 表单
     */
    private String filterStr;

    public String getFilterStr() {
        return filterStr;
    }

    public void setFilterStr(String filterStr) {
        this.filterStr = filterStr;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
