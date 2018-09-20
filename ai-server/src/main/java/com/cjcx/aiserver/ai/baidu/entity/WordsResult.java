package com.cjcx.aiserver.ai.baidu.entity;

import lombok.Data;

@Data
public class WordsResult {
    private Position location;

    private String words;
}
