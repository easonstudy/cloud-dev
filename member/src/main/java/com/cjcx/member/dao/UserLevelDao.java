package com.cjcx.member.dao;

import com.cjcx.member.dto.UserLevelDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLevelDao extends IBaseDao<UserLevelDto, Integer> {
}
