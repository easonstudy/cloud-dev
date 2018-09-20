package com.cjcx.pay.entity;


import com.cjcx.pay.framework.orm.BaseEntity;

public class SystemParam extends BaseEntity {

    public static final String KEY_MERCHANT_NUMBER      = "MERCHANT_NUMBER";

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
