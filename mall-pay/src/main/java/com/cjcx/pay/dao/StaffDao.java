package com.cjcx.pay.dao;


import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface StaffDao extends IBaseDao<StaffDto, Integer> {

    public void login();

    public StaffDto findByToken(String token);

}
