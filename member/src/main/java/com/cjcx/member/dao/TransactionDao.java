package com.cjcx.member.dao;

import com.cjcx.member.dto.TransactionDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends IBaseDao<TransactionDto, Integer> {

    void storageScore();

    TransactionDto searchTransaction(@Param("openId") String openId, @Param("uniqueId") String uniqueId);


}
