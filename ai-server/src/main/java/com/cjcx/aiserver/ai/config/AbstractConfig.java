package com.cjcx.aiserver.ai.config;

public abstract class AbstractConfig {

    /**
     * B A T
     */
    protected String action;

    /**
     * 待分析网络图片
     */
    protected String url;

    /**
     * 待分析图片Base64
     */
    protected String image;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public abstract String getHanderType();
}
