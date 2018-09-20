package com.cjcx.member.api.v1;

import com.cjcx.member.dto.ScoreRatioDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.score.IScoreRatioService;
import com.cjcx.member.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/score/ratio")
public class ScoreRatioController extends BaseController<ScoreRatioDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IScoreRatioService service;

    @Override
    protected IBaseService<ScoreRatioDto, Integer> getDefaultService() {
        return service;
    }

    /**
     * 新增或者修改
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public ResultObject merge(ScoreRatioDto dto) {
        ResultObject result = new ResultObject();
        try {
            logger.info("Ratio:" + dto.getIsDelete());
            result = getDefaultService().merge(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 列表
     *
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/list")
    public ResultObject list() {
        ResultObject result = new ResultObject();
        try {
            super.enabledPaging();
            List<ScoreRatioDto> list = service.findAll();
            result.setData(super.convertListToPageInfo(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 列表-all
     *
     * @return
     */
    @RequestMapping(value = "/list/all")
    public ResultObject listall() {
        ResultObject result = new ResultObject();
        try {
            List<ScoreRatioDto> list = service.findAll();
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
