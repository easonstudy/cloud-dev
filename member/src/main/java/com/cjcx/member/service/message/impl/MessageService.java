package com.cjcx.member.service.message.impl;

import com.cjcx.member.dao.MessageDao;
import com.cjcx.member.dto.MessageDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.message.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends BaseService<MessageDto, Integer> implements IMessageService {

    @Autowired
    MessageDao dao;

    @Override
    protected IBaseDao<MessageDto, Integer> getDao() {
        return dao;
    }

}
