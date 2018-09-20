package com.cjcx.member.api.v1;

import com.cjcx.member.dto.UserDto;
import com.cjcx.member.dto.UserRequestDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.WxEventMessage;
import com.cjcx.member.service.user.IUserRequestService;
import com.cjcx.member.service.user.IUserService;
import com.cjcx.member.utils.GsonUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController<UserDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService userService;

    @Autowired
    IUserRequestService userRequestService;

    @Override
    protected IBaseService<UserDto, Integer> getDefaultService() {
        return userService;
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable("id") Integer id) {
        Gson gson = new Gson();
        UserDto m = null;
        try {
            m = userService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(m);
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultObject deleteUser(@PathVariable("id") Integer id) {
        ResultObject result = new ResultObject();
        UserDto m = null;
        try {
            logger.info("USER DELETE:" + id);
            UserDto userDto = new UserDto();
            userDto.setId(id);
            userDto.setIsDelete(true);
            userService.updateById(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResultObject updateUser(@PathVariable("id") Integer id, @RequestBody UserDto userDto) {
        ResultObject result = new ResultObject();
        UserDto m = null;
        try {
            logger.info("USER PUT:" + userDto.toString());
            userService.updateById(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode(ResultObject.ERRCODE_NO);
        }
        return result;
    }

    /**
     * 用户登入 api,返回用户信息, 和用户访问token
     *
     * @param m     登入方式  1:手机登入
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping(value = "/login")
    public ResultObject loginWithPhone(@RequestParam("m") Integer m,
                                       @RequestParam(value = "phone", required = false) String phone,
                                       @RequestParam(value = "code", required = false) String code) {
        ResultObject r = new ResultObject();
        Map<String, Object> map = new HashMap<>();
        map.put("m", m);
        map.put("phone", phone);
        map.put("code", code);
        r = userService.login(map);

        logger.info("/login 返回消息:{}", GsonUtil.toJson(r));
        return r;
    }

    @RequestMapping(value = "/register")
    public String saveUser(@RequestParam("openId") String openId, @RequestParam("nickName") String nickName) {
        UserDto userDto = new UserDto();
        userDto.setOpenId(openId);
        userDto.setNickName(nickName);
        userDto.setCreateTime(new Date());
        userService.insert(userDto);
        return "";
    }


    /**
     * 提供给微信公共号 的接口
     *
     * @param eventMessage
     * @return
     */
    @RequestMapping(value = "/wx_subscribe")
    public ResultObject wxUserSubscribe(@RequestBody WxEventMessage eventMessage) {
        ResultObject result = null;
        result = userService.register(eventMessage);
        return result;
    }

    @RequestMapping(value = "/wx_unsubscribe")
    public ResultObject wxUserUnsubscribe(@RequestParam("openId") String username) {
        ResultObject result = null;
        result = userService.unbind(username);
        return result;
    }

    @RequestMapping(value = "/wx_userinfo")
    public ResultObject wxUserInfomation(@RequestParam("openId") String username) {
        ResultObject result = null;
        result = userService.getUserInfo(username);
        return result;
    }

    @AuthToken
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultObject list(@RequestParam(value = "nickName", required = false) String nickName) {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            UserDto condition = new UserDto();
            condition.setNickName(StringUtils.isEmpty(nickName) ? null : nickName);
            List<UserDto> list = userService.findUserListByCondition(condition);

            Page<UserDto> page = (Page<UserDto>) list;
            PageInfo<UserDto> pageInfo = new PageInfo<UserDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 完善资料
     *
     * @param userDto
     * @return
     */
    @RequestMapping(value = "/perfect")
    public ResultObject perfect(UserDto userDto) {
        ResultObject result = new ResultObject();
        try {
            Map map = userService.perfect(userDto);
            //Integer errorCode = Integer.parseInt(map.get(ResultObject.PARAM_ERRCODE).toString());
            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 申请测试
     *
     * @param userRequestDto
     * @return
     */
    @RequestMapping(value = "/request")
    public ResultObject perfect(UserRequestDto userRequestDto) {
        ResultObject result = new ResultObject();
        try {
            Map map = userRequestService.doRequestTest(userRequestDto);
            result.setErrCode(map.get(ResultObject.PARAM_ERRCODE).toString());
            result.setMsg(map.get(ResultObject.PARAM_MSG).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询会员 - 会员信息
     *
     * @param id 电话 openId 唯一的会员标识
     * @return
     */
    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public String searchUser(@PathVariable("id") String id) {
        Gson gson = new Gson();
        UserDto m = null;
        try {
            m = userService.getUserByLoginName(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.toJson(m);
    }




    /**
     * 积分查询(微信APP) 会员积分 和 积分列表
     *
     * @param id 会员id
     * @return
     */
    @RequestMapping(value = "/score")
    public ResultObject score(@RequestParam("id") Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        ResultObject result = new ResultObject();
        result = userService.wxAppScoreSearch(map);
        logger.info("/user/score 返回消息:" + GsonUtil.toJson(result));
        return result;
    }


}
