package com.cjcx.member.service.score.impl;

import com.cjcx.member.dao.ScoreSettingDao;
import com.cjcx.member.dto.ScoreSettingDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.score.IScoreSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreSettingService extends BaseService<ScoreSettingDto, Integer> implements IScoreSettingService {

    @Autowired
    ScoreSettingDao dao;

    @Override
    protected IBaseDao<ScoreSettingDto, Integer> getDao() {
        return dao;
    }
}
