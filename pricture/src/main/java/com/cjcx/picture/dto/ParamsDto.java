package com.cjcx.picture.dto;

public class ParamsDto {

    private String name;
    private Integer local = 0;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public Integer getLocal() {
        return local;
    }

    public void setLocal(Integer local) {
        this.local = local;
    }
}
