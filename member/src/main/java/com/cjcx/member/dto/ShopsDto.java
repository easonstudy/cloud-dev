package com.cjcx.member.dto;

import com.cjcx.member.entity.Shops;

public class ShopsDto extends Shops {

    private String ratioName;

    private Double ratio;

    public String getRatioName() {
        return ratioName;
    }

    public void setRatioName(String ratioName) {
        this.ratioName = ratioName;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
