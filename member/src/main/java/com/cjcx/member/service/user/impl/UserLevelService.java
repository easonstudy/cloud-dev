package com.cjcx.member.service.user.impl;

import com.cjcx.member.dao.UserLevelDao;
import com.cjcx.member.dto.UserLevelDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserLevelService;
import com.cjcx.member.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserLevelService extends BaseService<UserLevelDto, Integer> implements IUserLevelService {

    @Autowired
    UserLevelDao dao;

    @Override
    protected IBaseDao<UserLevelDto, Integer> getDao() {
        return dao;
    }

}
