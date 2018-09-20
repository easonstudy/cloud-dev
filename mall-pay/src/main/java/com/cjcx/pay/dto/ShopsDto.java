package com.cjcx.pay.dto;

import com.cjcx.pay.entity.Shops;
import com.cjcx.pay.entity.ShopsDevice;

import java.util.List;



public class ShopsDto extends Shops {

    private List<ShopsDevice> deviceList;

    private String deviceIds;

    public List<ShopsDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<ShopsDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }
}
