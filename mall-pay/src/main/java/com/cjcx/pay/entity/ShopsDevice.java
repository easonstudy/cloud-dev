package com.cjcx.pay.entity;

import com.cjcx.pay.framework.orm.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * t_shops_device
 * @author 
 */
@Data
public class ShopsDevice extends BaseEntity implements Serializable {
    private Integer shopsId;

    private String deviceId;
}