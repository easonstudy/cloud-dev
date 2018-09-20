package com.cjcx.member.dao;

import com.cjcx.member.dto.ScoreRatioDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRatioDao extends IBaseDao<ScoreRatioDto, Integer> {
}