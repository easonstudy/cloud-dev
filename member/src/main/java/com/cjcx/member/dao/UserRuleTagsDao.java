package com.cjcx.member.dao;

import com.cjcx.member.dto.UserRuleTagsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRuleTagsDao extends IBaseDao<UserRuleTagsDto, Integer> {
}
