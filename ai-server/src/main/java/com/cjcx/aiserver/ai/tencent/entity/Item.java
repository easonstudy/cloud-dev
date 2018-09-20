package com.cjcx.aiserver.ai.tencent.entity;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Itemcoord itemcoord;

    private List<Word> words;

    private String itemstring;
}
