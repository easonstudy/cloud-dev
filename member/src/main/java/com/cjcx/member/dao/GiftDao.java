package com.cjcx.member.dao;

import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftDao extends IBaseDao<GiftDto, Integer> {

    List<GiftDto> searchByCondition(GiftDto dto);
}
