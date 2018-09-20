package com.cjcx.member.entity;


import com.cjcx.member.framework.orm.BaseEntity;

import java.io.Serializable;

public class SystemParam extends BaseEntity {

    public static final String KEY_RECEIPT_FOR_QR_CODE    = "RECEIPT_FOR_QR_CODE";
    public static final String KEY_REQUEST_EMAIL      = "REQUEST_EMAIL";


    protected String paramName;

    protected String paramKey;

    protected String paramValue;

    protected String remark;

    protected String ordering;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }
}
