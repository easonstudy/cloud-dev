package com.cjcx.member.service.score;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.dto.TransactionGiftDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;

import java.util.List;

public interface ITransactionGiftService extends IBaseService<TransactionGiftDto, Integer> {


    ResultObject doTransactionGift(GiftDto dto);

    public List<TransactionGiftDto> getTransactionGiftList(FilterDto dto);
}
