package com.cjcx.wechat.open.service.weixin;

import com.cjcx.wechat.open.object.WxEventMessage;

import java.util.Map;

public interface IWxMessageService {

    /**
     * 事件消息
     * @param eventMessage
     * @return
     */
    Map<String, Object> eventMessageHandler(WxEventMessage eventMessage);

    Map<String, Object> textMessageHandler(WxEventMessage eventMessage);

}
