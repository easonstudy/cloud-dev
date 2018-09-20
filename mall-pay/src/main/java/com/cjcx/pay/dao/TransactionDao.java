package com.cjcx.pay.dao;

import com.cjcx.pay.dto.TransactionDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDao extends IBaseDao<TransactionDto, Integer> {

    List<TransactionDto> getList(FilterDto filterDto);

    List<TransactionDto> getVerfiyList(TransactionDto dto);
}
