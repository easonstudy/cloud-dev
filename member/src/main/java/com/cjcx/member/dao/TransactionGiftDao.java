package com.cjcx.member.dao;

import com.cjcx.member.dto.TransactionGiftDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionGiftDao extends IBaseDao<TransactionGiftDto, Integer> {

    void storageGiftExchange();

    List<TransactionGiftDto> getTransactionGiftList(FilterDto dto);
}
