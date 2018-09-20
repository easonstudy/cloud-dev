package com.cjcx.member.service.score.impl;

import com.cjcx.member.dao.ScoreAuditReceiptDao;
import com.cjcx.member.dto.ScoreAuditReceiptDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.service.score.IScoreAuditReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreAuditReceiptService extends BaseService<ScoreAuditReceiptDto, Integer> implements IScoreAuditReceiptService {

    @Autowired
    ScoreAuditReceiptDao dao;

    @Override
    protected IBaseDao<ScoreAuditReceiptDto, Integer> getDao() {
        return dao;
    }
}
