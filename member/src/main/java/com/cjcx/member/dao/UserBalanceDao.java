package com.cjcx.member.dao;


import com.cjcx.member.dto.UserBalanceDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *
 */
@Repository
public interface UserBalanceDao extends IBaseDao<UserBalanceDto, Integer> {

    public int updateScore(@Param("id") Integer id, @Param("score") BigDecimal score);

}
