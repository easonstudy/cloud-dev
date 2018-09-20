package com.cjcx.member.service.score.impl;

import com.cjcx.member.dao.ScoreRatioDao;
import com.cjcx.member.dto.ScoreRatioDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.score.IScoreRatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreRatioService extends BaseService<ScoreRatioDto, Integer> implements IScoreRatioService {

    @Autowired
    ScoreRatioDao dao;

    @Override
    protected IBaseDao<ScoreRatioDto, Integer> getDao() {
        return dao;
    }
}
