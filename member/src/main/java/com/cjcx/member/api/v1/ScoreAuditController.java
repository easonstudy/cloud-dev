package com.cjcx.member.api.v1;

import com.cjcx.member.dto.ScoreAuditDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.entity.Staff;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.score.IScoreAuditService;
import com.cjcx.member.utils.GsonUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/score/audit")
public class ScoreAuditController extends BaseController<ScoreAuditDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IScoreAuditService service;

    @Override
    protected IBaseService<ScoreAuditDto, Integer> getDefaultService() {
        return service;
    }

    /**
     * 待审核(图片) 处理
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/submit")
    public ResultObject audit(@RequestBody ScoreAuditDto dto) {
        ResultObject result = new ResultObject();
        result = service.doScoreAudit(dto);
        return result;
    }

    /**
     * 未审核(图片) 提交
     *
     * @param dto 图片路径
     * @return
     */
    @RequestMapping(value = "/image")
    public ResultObject uploadImage(@RequestBody ScoreAuditDto dto) {
        ResultObject result = new ResultObject();
        result = service.addScoreAudit(dto);
        return result;
    }


    /**
     * 审核列表
     *
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/list")
    public ResultObject auditList(@RequestParam("state") Integer state) {
        ResultObject result = new ResultObject();
        try {
            PageParams params = getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            ScoreAuditDto condition = new ScoreAuditDto();
            condition.setState(state);
            if (state == 2) {
                Staff staff = super.getStaff();
                condition.setStaffId(staff.getId());
            }
            List<ScoreAuditDto> list = service.findByCondition(condition);
            if (params.isEnabled()) {
                result.setData(super.convertListToPageInfo(list));
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/pull")
    public ResultObject auditPull(@RequestParam("id") Integer id) {
        ResultObject result = new ResultObject();
        try {
            Staff staff = super.getStaff();
            Integer errorCode = service.pull(id, staff);
            result.setErrCode(errorCode + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 审核历史
     *
     * @return
     */
    @RequestMapping(value = "/history")
    public ResultObject history(@RequestBody(required = false) FilterDto dto) {
        ResultObject result = new ResultObject();
        try {
            super.enabledPaging();
            if (dto != null && dto.getTicketType() != null) {
                if (dto.getTicketType() == 0) {
                    PageHelper.orderBy("a.create_time DESC");
                } else if (dto.getTicketType() == 2) {
                    PageHelper.orderBy("a.update_time DESC");
                }
            }
            logger.info("审核历史 条件:{}", GsonUtil.toJson(dto));
            List<ScoreAuditDto> list = service.getScoreAuditDtoList(dto);


            result.setData(super.convertListToPageInfo(list));

            logger.info("审核历史 返回消息:{}", GsonUtil.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
