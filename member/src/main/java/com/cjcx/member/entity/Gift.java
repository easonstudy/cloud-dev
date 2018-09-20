package com.cjcx.member.entity;

import com.cjcx.member.framework.orm.BaseEntity;
import com.cjcx.member.utils.DateUtil;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * t_gift
 *
 * @author
 */
public class  Gift extends BaseEntity {

    /**
     * 产品类型  1:优惠券抵扣
     */
    protected Integer type;

    /**
     * 礼品编号
     */
    protected String code;

    /**
     * 礼品名称
     */
    protected String name;

    /**
     * 所需积分
     */
    protected Double score;

    /**
     * 礼品数量
     */
    protected Integer number;

    /**
     * 礼品图像地址
     */
    protected String url;

    /**
     * 礼品过期时间
     */
    protected Date expiredTime;

    protected boolean isDelete;

    protected String expiredTimeStr;

    public String getExpiredTimeStr() {
        //return expiredTimeStr;
        if(expiredTime != null)
            return DateUtil.convertToStr(
                    LocalDateTime.ofInstant(expiredTime.toInstant(), ZoneId.systemDefault()),
                    "yyyy-MM-dd");
        return null;
    }

    public void setExpiredTimeStr(String expiredTimeStr) {
        this.expiredTimeStr = expiredTimeStr;
        try {
            //this.setExpiredTime(Date.from(DateUtil.convertToDate(expiredTimeStr, "yyyy-MM-dd"));

            this.setExpiredTime(DateUtil.DF_yyyyMMdd.parse(expiredTimeStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
}