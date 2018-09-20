package com.cjcx.aiserver.ai.baidu.entity;

import lombok.Data;

import java.util.List;

@Data
public class WordsResponse {
	private Integer error_code = 0;
	private String error_msg;
	
	private String log_id;
	private Integer direction;
	private Integer words_result_num;
	private List<WordsResult> words_result;
}
