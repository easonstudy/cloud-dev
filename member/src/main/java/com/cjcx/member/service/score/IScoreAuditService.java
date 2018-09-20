package com.cjcx.member.service.score;

import com.cjcx.member.dto.ScoreAuditDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.entity.Staff;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;

import java.util.List;

public interface IScoreAuditService extends IBaseService<ScoreAuditDto, Integer> {

    /**
     * 拉取 未审核记录
     * @param id
     * @param staff
     * @return
     */
    Integer pull(Integer id, Staff staff);

    /**
     * 审核 兑换积分
     * @param dto
     * @return
     */
    ResultObject doScoreAudit(ScoreAuditDto dto);
    ResultObject addScoreAudit(ScoreAuditDto dto);


    List<ScoreAuditDto> getScoreAuditDtoList(FilterDto dto);
}
