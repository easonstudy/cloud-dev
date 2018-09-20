package com.cjcx.wechat.open.service.member;

import com.cjcx.wechat.open.config.MemberConfig;
import com.cjcx.wechat.open.dto.UserDto;
import com.cjcx.wechat.open.entity.UserBalance;
import com.cjcx.wechat.open.object.WxEventMessage;
import com.cjcx.wechat.open.utils.GsonUtil;
import com.cjcx.wechat.open.utils.JacksonUtil;
import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.web.ResultObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weixin.popular.util.JsonUtil;

@Service
public class UserService implements IUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public ResultObject register(WxEventMessage eventMessage) {
        ResultObject r = new ResultObject();
        try {
            String openId = eventMessage.getFromUserName();
            //{"errorCode":"0"}
            String result = RequestUtil.doPostByJson(MemberConfig.url_register, GsonUtil.toJson(eventMessage));
            logger.info("关注:" + openId);
            logger.info("结果:{}" , result);
            r = JacksonUtil.json2Bean(result, ResultObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }


    @Override
    public ResultObject unbind(WxEventMessage eventMessage) {
        ResultObject r = new ResultObject();
        try {
            String openId = eventMessage.getFromUserName();

            String result = RequestUtil.doPost(MemberConfig.url_unbind, "openId="+openId);
            logger.info("取消关注 OpenId:" + openId);
            logger.info("结果:{}" , result);
            r = JacksonUtil.json2Bean(result, ResultObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public UserDto getUserByLoginName(String loginName) {
        UserDto dto = null;
        ResultObject r = new ResultObject();
        try {
            String result = RequestUtil.doPost(MemberConfig.url_user_info, "openId="+loginName);
            logger.info("获取用户信息 OpenId:" + loginName);
            logger.info("结果:{}" , result);
            r = JacksonUtil.json2Bean(result, ResultObject.class);
            if(r.isOk()) {
                JSONObject object = new JSONObject(result);
                dto = JacksonUtil.json2Bean(object.get("data").toString(), UserDto.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }
}
