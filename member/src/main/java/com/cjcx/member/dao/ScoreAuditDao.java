package com.cjcx.member.dao;

import com.cjcx.member.dto.ScoreAuditDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.orm.IBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreAuditDao extends IBaseDao<ScoreAuditDto, Integer> {

    void storageScoreAudit();

    List<ScoreAuditDto> getScoreAuditList(FilterDto dto);

}
