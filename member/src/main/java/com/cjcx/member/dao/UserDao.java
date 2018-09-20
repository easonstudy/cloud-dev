package com.cjcx.member.dao;


import com.cjcx.member.dto.UserDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface UserDao extends IBaseDao<UserDto, Integer> {

    UserDto findByLoginName(String loginName);

    List<UserDto> findUserListByCondition(UserDto userDto);
}
