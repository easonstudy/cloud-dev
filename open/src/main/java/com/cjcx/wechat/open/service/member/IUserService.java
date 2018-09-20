package com.cjcx.wechat.open.service.member;

import com.cjcx.wechat.open.dto.UserDto;
import com.cjcx.wechat.open.object.WxEventMessage;
import com.cjcx.wechat.open.web.ResultObject;

public interface IUserService {

    /**
     * 注册
     * @param eventMessage
     * @return
     */
    public ResultObject register(WxEventMessage eventMessage);

    /**
     * 注册-解绑
     * @param eventMessage
     * @return
     */
    public ResultObject unbind(WxEventMessage eventMessage);

    /**
     * 个人资料查询
     * @param loginName openId
     * @return
     */
    public UserDto getUserByLoginName(String loginName);


}
