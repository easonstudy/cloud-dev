package com.cjcx.wechat.open.utils;

import com.cjcx.wechat.open.config.WeixinConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.support.TokenManager;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeixinUtil extends WeixinConfig {

    private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);

    /**
     * 响应内容
     */
    public static String RESPONSE_VALUE = "value";
    public static String RESPONSE_TYPE = "type";

    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";


    /**
     * 事件类型：scancode_push(扫码推事件的事件推送)
     */
    public static final String EVENT_TYPE_SCANCODE_PUSH = "scancode_push";
    public static final String EVENT_TYPE_SCANCODE_WAITMSG = "scancode_waitmsg";
    public static final String SCANCODE_PUSH_KEY_ADD_SCORE = "add_score";


    /**
     * 菜单 CLICK(点击)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 菜单VIEW(点击URL跳转)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";


    /**
     * 自定义事件
     */
    public static final String EVENT_TYPE_REGISTER = "register";

    /**
     * 自定义事件 分隔符号
     */
    public static final String EVENT_SEP = "===";


    /**
     * 关注响应内容 => 回复客户
     */
    public static Map<String, String> resMap = new HashMap<>();

    static {
        resMap.put(EVENT_TYPE_SUBSCRIBE, "您好，欢迎关注");
    }

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    public static String getAccessToken() {
        //return "6_QnyWUwfcojV-vv1uQiYu3vLI5EdTzhIlktWjyTVdwdKT8j3H8-OS8mfPfW1I5kdnKLge8yrC2mGvg1To2EIcQUf9BiDyR8y23bZ1mZ3DYovgET7P0N7Ejt7DCsscxIxxICuatY9k63lOJDy8WMEgABAQGY";
        return TokenManager.getToken(WeixinConfig.APPID);
    }


    public static String getReceiptForQrcode(String url, String receiptId) throws Exception {
        return RequestUtil.doPostByJson(url, "{'receiptId' : '" + receiptId + "' }");
    }

    /**
     * 创建临时二维码
     *
     * @param b         true字符  false 数字
     * @param scene_str 内容
     * @return 二维码地址
     * @throws Exception
     */
    public static String createTempQrcode(boolean b, String scene_str) {
        String param = null;
        try {
            if (b) {
                param = "{\"expire_seconds\": 604800, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"test\"}}}";
            } else {
                param = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + Integer.parseInt(scene_str) + "}}}";
            }

            String url = URL_QRCODE_CREATE + getAccessToken();
            logger.debug("CreateTempQrcode Request url:" + url);
            String content = RequestUtil.doPostByJson(url, param);
            /*正确的Json返回结果:
            {"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm
                3sUw==","expire_seconds":60,"url":"http:\/\/weixin.qq.com\/q\/kZgfwMTm72WWPkovabbI"}
            */
            logger.debug("CreateTempQrcode Content:" + content);
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(content, Map.class);
            Object object = map.get("ticket");
            if (object != null) {
                String ticket = object.toString();
                //TICKET记得进行UrlEncode
                ticket = URLEncoder.encode(ticket, "UTF-8");
                //图片地址
                logger.debug("CreateTempQrcode Ticket URL:" + (URL_SHOWQRCODE + ticket));
                return URL_SHOWQRCODE + ticket;
            } else {
                logger.debug("CreateTempQrcode errcode:" + map.get("errcode") + " errmsg:" + map.get("errmsg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
