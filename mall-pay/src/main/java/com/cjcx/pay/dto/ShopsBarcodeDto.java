package com.cjcx.pay.dto;

import com.cjcx.pay.entity.ShopsBarcode;

public class ShopsBarcodeDto extends ShopsBarcode {

    private String shopsName;

    public String getShopsName() {
        return shopsName;
    }
    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }


    @Override
    public String toString() {
        return "ShopsBarcodeDto{" +
                "shopsName='" + shopsName + '\'' + super.toString() +
                '}';
    }
}
