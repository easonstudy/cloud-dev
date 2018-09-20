package com.cjcx.member.api.v1;

import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.dto.UserLevelDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 等级设置
 */
@RestController
@RequestMapping("/api/v1/userlevel")
public class UserLevelController extends BaseController<UserLevelDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    IUserLevelService userLevelService;

    @Override
    protected IBaseService<UserLevelDto, Integer> getDefaultService() {
        return userLevelService;
    }

    /**
     * 新增或者修改
     *
     * @param dto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public ResultObject merge(UserLevelDto dto) {
        ResultObject result = new ResultObject();
        try {
            result = userLevelService.merge(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultObject deleteStaff(@PathVariable("id") Integer id) {
        ResultObject result = new ResultObject();
        StaffDto m = null;
        try {
            logger.info("USER Level delete:" + id);
            userLevelService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/list")
    public ResultObject list() {
        ResultObject result = new ResultObject();
        try {
            List<UserLevelDto> list = userLevelService.findAll();
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
