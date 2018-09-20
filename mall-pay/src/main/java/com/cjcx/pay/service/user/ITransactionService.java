package com.cjcx.pay.service.user;


import com.cjcx.pay.dto.TransactionDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.dto.filter.PayDto;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.ResultObject;

import java.util.List;

public interface ITransactionService extends IBaseService<TransactionDto, Integer> {

    List<TransactionDto> getTransactionList(FilterDto filterDto);

    public ResultObject storedTransaction(PayDto dto);

}
