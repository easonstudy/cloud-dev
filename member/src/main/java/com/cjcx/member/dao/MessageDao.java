package com.cjcx.member.dao;

import com.cjcx.member.dto.MessageDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDao extends IBaseDao<MessageDto, Integer> {

}
