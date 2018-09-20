package com.cjcx.member.dao;


import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface StaffDao extends IBaseDao<StaffDto, Integer>{

    public void login();

    public StaffDto findByToken(String token);

}
