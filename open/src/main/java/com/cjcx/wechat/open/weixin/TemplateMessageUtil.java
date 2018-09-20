package com.cjcx.wechat.open.weixin;

import com.cjcx.wechat.open.utils.WeixinUtil;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class TemplateMessageUtil extends WeixinUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    //积分兑换通知 id
    private static final String message_transaction = "syZcgSAHwq9YD1Po9mQ5LobOsdX46IhMWtTcnwh5g54";
    //积分兑换通知
    public static void messageTransaction(String openId, double points, String result, boolean isSuccess){
        TemplateMessage message = new TemplateMessage();
        message.setTouser(openId);
        message.setTemplate_id("syZcgSAHwq9YD1Po9mQ5LobOsdX46IhMWtTcnwh5g54");
        LinkedHashMap<String, TemplateMessageItem> data = new LinkedHashMap<String, TemplateMessageItem>();
        if(isSuccess) {
            data.put("first", new TemplateMessageItem("恭喜你,兑换成功", "#173177"));
            data.put("productType", new TemplateMessageItem("产品名", "#173177"));
            data.put("name", new TemplateMessageItem("积分", "#173177"));
            data.put("points", new TemplateMessageItem(points + "", "#173177"));
            data.put("date", new TemplateMessageItem(sdf.format(new Date()), "#173177"));
            data.put("remark", new TemplateMessageItem("您的当前积分值是" + result + "分", "#173177"));
        } else {
            data.put("first", new TemplateMessageItem("抱歉,兑换失败", "#173177"));
            data.put("productType", new TemplateMessageItem("产品名", "#173177"));
            data.put("name", new TemplateMessageItem("积分", "#173177"));
            data.put("points", new TemplateMessageItem("0", "#173177"));
            data.put("date", new TemplateMessageItem(sdf.format(new Date()), "#173177"));
            data.put("remark", new TemplateMessageItem("错误:" + result, "#173177"));
        }
        message.setData(data);
        MessageAPI.messageTemplateSend(getAccessToken(), message);
    }

}
