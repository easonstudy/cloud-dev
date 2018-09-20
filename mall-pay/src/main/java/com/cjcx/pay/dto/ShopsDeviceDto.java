package com.cjcx.pay.dto;

import com.cjcx.pay.entity.ShopsDevice;

public class ShopsDeviceDto extends ShopsDevice {
    private String shopsName;

    public String getShopsName() {
        return shopsName;
    }
    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

}
