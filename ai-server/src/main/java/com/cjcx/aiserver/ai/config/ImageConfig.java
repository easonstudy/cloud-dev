package com.cjcx.aiserver.ai.config;

import lombok.Data;

/**
 * 图像配置
 */
@Data
public class ImageConfig extends AbstractConfig {

    public static final String TYPE = "_image";

    /**
     * 识别级别, 0: 普通, 1: 高精度(默认), 2: 高精度&位置
     */
    private Integer level = 1;

    @Override
    public String getHanderType() {
        return action + TYPE;
    }
}
