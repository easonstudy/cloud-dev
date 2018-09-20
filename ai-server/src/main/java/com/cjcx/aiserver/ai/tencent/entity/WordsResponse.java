package com.cjcx.aiserver.ai.tencent.entity;

import lombok.Data;

@Data
public class WordsResponse {
    private int code;
    private String message;
    private ItemsData data;
}
