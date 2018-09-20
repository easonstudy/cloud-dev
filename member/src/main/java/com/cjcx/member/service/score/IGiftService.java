package com.cjcx.member.service.score;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.framework.service.IBaseService;

import java.util.List;

public interface IGiftService extends IBaseService<GiftDto, Integer> {

    public List<GiftDto> searchByCondition(GiftDto dto);

}
