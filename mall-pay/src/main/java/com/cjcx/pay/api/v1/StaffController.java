package com.cjcx.pay.api.v1;

import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.framework.annotation.AuthToken;
import com.cjcx.pay.framework.controller.BaseController;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.PageParams;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.ILoginService;
import com.cjcx.pay.service.staff.IStaffService;
import com.cjcx.pay.utils.IpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class StaffController extends BaseController<StaffDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ILoginService loginService;

    @Autowired
    IStaffService staffService;

    @Override
    protected IBaseService<StaffDto, Integer> getDefaultService() {
        return staffService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "pwd", required = false) String pwd,
                        @RequestParam(value = "identity", required = false) String identity) {
        String result = loginService.login(username, pwd);

        logger.info("Login user:" + username + ", pwd:" + pwd + ", ip:" + IpUtil.getIp(request) + " Result: " + result);
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResultObject logout() {
        ResultObject result = new ResultObject();
        loginService.logout();
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/changePassword", method = RequestMethod.PATCH)
    public ResultObject changePassword(@RequestParam("oldPwd") String oldPwd,
                                       @RequestParam("newPwd") String newPwd) {
        ResultObject result = new ResultObject();
        try {
            String token = super.getAuthorizationToken();
            //logger.info("Authorization" + token);
            Map map = staffService.changePassword(token, oldPwd, newPwd);

            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/changeProfile", method = RequestMethod.PATCH)
    public ResultObject changeProfile(@RequestParam(value = "nickName", required = false) String nickName,
                                      @RequestParam(value = "email", required = false) String email) {
        ResultObject result = new ResultObject();
        try {
            String token = super.getAuthorizationToken();
            Map map = staffService.changeProfile(token, nickName, email);
            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/staff/list", method = RequestMethod.GET)
    public ResultObject list() {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            StaffDto condition = new StaffDto();
            List<StaffDto> list = staffService.findByCondition(condition);

            Page<StaffDto> page = (Page<StaffDto>) list;
            PageInfo<StaffDto> pageInfo = new PageInfo<StaffDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/staff/{id}", method = RequestMethod.DELETE)
    public ResultObject deleteStaff(@PathVariable("id") Integer id) {
        ResultObject result = new ResultObject();
        StaffDto m = null;
        try {
            logger.info("USER DELETE:" + id);
            StaffDto staff = new StaffDto();
            staff.setId(id);
            staff.setIsDelete(true);
            staffService.updateById(staff);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/staff/changePassword", method = RequestMethod.PATCH)
    public ResultObject staffChangePassword(StaffDto staffDto) {
        ResultObject result = new ResultObject();
        try {
            Map map = staffService.changePassword(staffDto);
            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 新增或者修改
     *
     * @param staffDto
     * @return
     */
    @AuthToken
    @RequestMapping(value = "/staff/merge", method = RequestMethod.POST)
    public ResultObject merge(StaffDto staffDto) {
        ResultObject result = new ResultObject();
        try {
            result = staffService.merge(staffDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
