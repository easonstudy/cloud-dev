package com.cjcx.member.service.user.impl;

import com.cjcx.member.dao.UserRuleTagsDao;
import com.cjcx.member.dto.UserRuleTagsDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.user.IUserRuleTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRuleTagsService extends BaseService<UserRuleTagsDto, Integer> implements IUserRuleTagsService {

    @Autowired
    UserRuleTagsDao dao;

    @Override
    protected IBaseDao<UserRuleTagsDto, Integer> getDao() {
        return dao;
    }


}
