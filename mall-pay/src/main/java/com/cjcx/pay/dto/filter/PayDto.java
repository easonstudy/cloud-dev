package com.cjcx.pay.dto.filter;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayDto implements Serializable{

    private String deviceId;
    private String barcode;
    private String date;

}
