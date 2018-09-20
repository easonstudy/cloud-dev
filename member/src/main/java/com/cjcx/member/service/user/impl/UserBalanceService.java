package com.cjcx.member.service.user.impl;

import com.cjcx.member.dao.UserBalanceDao;
import com.cjcx.member.dto.UserBalanceDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.user.IUserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBalanceService extends BaseService<UserBalanceDto, Integer> implements IUserBalanceService {

    @Autowired
    UserBalanceDao userBalanceDao;

    @Override
    protected IBaseDao<UserBalanceDto, Integer> getDao() {
        return userBalanceDao;
    }
}
