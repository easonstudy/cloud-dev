package com.cjcx.aiserver.ai.config;

import lombok.Data;

@Data
public class FaceConfig extends AbstractConfig{

    public static final String TYPE = "_face";

    @Override
    public String getHanderType() {
        return action + TYPE;
    }
}
