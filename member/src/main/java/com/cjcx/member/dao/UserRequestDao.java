package com.cjcx.member.dao;


import com.cjcx.member.dto.UserRequestDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface UserRequestDao extends IBaseDao<UserRequestDto, Integer> {

    List<UserRequestDto> findByUserRequest(UserRequestDto userRequestDto);
}
