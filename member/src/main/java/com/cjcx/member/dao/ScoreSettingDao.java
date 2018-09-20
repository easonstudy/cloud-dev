package com.cjcx.member.dao;

import com.cjcx.member.dto.ScoreSettingDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreSettingDao extends IBaseDao<ScoreSettingDto, Integer> {
}
