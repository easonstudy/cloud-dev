package com.cjcx.member.service.user;


import com.cjcx.member.dto.UserDto;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.WxEventMessage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IUserService extends IBaseService<UserDto, Integer> {

    /**
     * 登入接口
     * @param map
     * @return
     */
    ResultObject login(Map<String, Object> map);

    /**
     * 注册 微信关注
     * @param eventMessage
     * @return
     */
    ResultObject register(WxEventMessage eventMessage);


    /**
     * 解绑 微信取消关注
     * @return
     */
    ResultObject unbind(String username);

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    ResultObject getUserInfo(String username);

    UserDto searchUserById(String id);
    UserDto getUserByLoginName(String loginName);

    List<UserDto> findUserListByCondition(UserDto userDto);

    Map<String, Object> perfect(UserDto userDto);


    ResultObject sendSmsCode(String phone, Integer type);

    ResultObject wxAppScoreSearch(Map<String, Object> map);


}
