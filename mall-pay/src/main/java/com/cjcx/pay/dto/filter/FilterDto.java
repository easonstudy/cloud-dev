package com.cjcx.pay.dto.filter;

import lombok.Data;

/**
 * 列表 查询过滤 公共dto
 */
@Data
public class FilterDto {
    private Integer shopsId;
    /**
     * 条码查询列表
     */
    private String shopsName;
    private String wechatCode;
    private String barcode;
    private String name;


    /**
     * 设备id
     */
    private String deviceId;
}
