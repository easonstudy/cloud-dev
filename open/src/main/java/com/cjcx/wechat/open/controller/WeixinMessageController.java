package com.cjcx.wechat.open.controller;

import com.cjcx.wechat.open.config.WeixinConfig;
import com.cjcx.wechat.open.object.WxEventMessage;
import com.cjcx.wechat.open.service.weixin.IWxMessageService;
import com.cjcx.wechat.open.utils.ImgUtil;
import com.cjcx.wechat.open.utils.QrcodeUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.api.MediaAPI;
import weixin.popular.bean.media.Media;
import weixin.popular.bean.media.MediaType;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.xmlmessage.XMLImageMessage;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.bean.xmlmessage.XMLNewsMessage;
import weixin.popular.bean.xmlmessage.XMLTextMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 接收 公众号 消息
 */
@RestController
@RequestMapping("/wx")
public class WeixinMessageController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IWxMessageService wxMessageService;

    //重复通知过滤
    private static ExpireKey expireKey = new DefaultExpireKey();

    /**
     * 接收公众号消息
     */
    @RequestMapping(value = "/receive")
    public void receive(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            ServletInputStream inputStream = request.getInputStream();
            outputStream = response.getOutputStream();
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");

            //首次请求申请验证,返回echostr
            if (echostr != null) {
                outputStreamWrite(outputStream, echostr);
                return;
            }

            logger.info("Get Wechat Parameter==================================================START");
            Enumeration em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String name = (String) em.nextElement();
                String value = request.getParameter(name);
                logger.info(name + ":" + value);
            }
            logger.info("Get Wechat Parameter==================================================END");

            //验证请求签名
            if (!signature.equals(SignatureUtil.generateEventMessageSignature(WeixinConfig.token, timestamp, nonce))) {
                logger.info("The request signature is invalid");
                return;
            }

            //处理消息
            handlerMessage(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                outputStreamWrite(outputStream, "success");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 事件处理
     *
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    public void handlerMessage(ServletInputStream inputStream, ServletOutputStream outputStream) throws Exception {
        WxEventMessage eventMessage = XMLConverUtil.convertToObject(WxEventMessage.class, inputStream);
        String key = eventMessage.getFromUserName() + "__"
                + eventMessage.getToUserName() + "__"
                + eventMessage.getMsgId() + "__"
                + eventMessage.getCreateTime();
        if (expireKey.exists(key)) {
            //重复通知不作处理
            return;
        } else {
            expireKey.add(key);
        }

        String openId = eventMessage.getFromUserName();
        String msgType = eventMessage.getMsgType();
        logger.info("MsgType:" + msgType + ", OpenId:" + openId);
        Map<String, Object> map = new HashMap<>();
        switch (msgType) {
            //事件
            case "event":
                map = wxMessageService.eventMessageHandler(eventMessage);
                break;
            //文本
            case "text":
                map = wxMessageService.textMessageHandler(eventMessage);
                break;
            default:
                break;
        }

        if (map.get(WeixinUtil.RESPONSE_TYPE) == null) {
            logger.info("不返回消息到微信公众号");
            return;
        }

        String type = map.get(WeixinUtil.RESPONSE_TYPE).toString();
        switch (type) {
            case WeixinUtil.RESP_MESSAGE_TYPE_TEXT:
                responseTextMessage(outputStream, eventMessage, map.get(WeixinUtil.RESPONSE_VALUE).toString());
                break;
            //case WeixinUtil.REQ_MESSAGE_TYPE_IMAGE:
            //    receiptImage(outputStream, eventMessage);
            //    break;
            case WeixinUtil.RESP_MESSAGE_TYPE_NEWS:
                responseNewMessage(outputStream, eventMessage, (NewsMessage) map.get(WeixinUtil.RESPONSE_VALUE));
                break;
            case "scoreImage":
                scoreImage(outputStream, eventMessage, map.get(WeixinUtil.RESPONSE_VALUE).toString());
                break;
            default:
                break;
        }
    }

    /**
     * 测试 - 积分二维码 - 返回拼接的图片
     *
     * @param outputStream
     * @param eventMessage
     * @param receiptId
     */
    private void scoreImage(ServletOutputStream outputStream, WxEventMessage eventMessage, String receiptId) {
        //接口凭证
        String access_token = WeixinUtil.getAccessToken();

        String openId = eventMessage.getFromUserName();

        String qrimgContent = "http://api.android-pos.cn/api/v1/transaction/score?openId=" + openId + "&receiptId=" + receiptId;
        logger.info("ScoreImage内容:" + qrimgContent);
        //InputStream paramInstream = QrcodeUtil.getQrcodeInputStream(qrimgContent);

        //二维码
        String qrImgPath = "/opt/images/qr-img-" + receiptId + ".png";
        QrcodeUtil.createQrImage(qrImgPath, qrimgContent);

        //随机取一个小票图片
        //String [] receiptImgArr = {"104","108","389","527","561"};
        String receiptImgPath = "/opt/images/" + receiptId.split("_")[1] + ".png";
        //生成拼接的图片
        String outputPath = "/opt/images/new.png";
        String[] files = {receiptImgPath, qrImgPath};
        ImgUtil.mergeImage(files, 2, outputPath);

        File newFile = new File(outputPath);

        // 上传图片到素材管理 3天有效  https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726
        Media media = MediaAPI.mediaUpload(access_token, MediaType.image, newFile);
        if (media == null) {
            responseTextMessage(outputStream, eventMessage, "-2");
            return;
        }

        //获取media_id
        String media_id = media.getMedia_id();
        responseImageMessage(outputStream, eventMessage, media_id);
    }

    private void receiptImage(ServletOutputStream outputStream, WxEventMessage eventMessage) {
        //接口凭证
        String access_token = WeixinUtil.getAccessToken();

        //生成参数二维码 dom随机小票号
        String receiptId = (int) Math.random() * 1000 + "";
        String imageContent = WeixinUtil.URL_OAUTH2_AUTHORIZE_BASE.replace("[state]", receiptId);
        logger.info("Image内容:" + imageContent);
        InputStream paramInstream = QrcodeUtil.getQrcodeInputStream(imageContent);

        // 上传图片到素材管理 3天有效  https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726
        Media media = MediaAPI.mediaUpload(access_token, MediaType.image, paramInstream);
        if (media == null) {
            responseTextMessage(outputStream, eventMessage, "-2");
            return;
        }

        //获取media_id
        String media_id = media.getMedia_id();
        responseImageMessage(outputStream, eventMessage, media_id);
    }

    /**
     * 被动回复 文本消息
     *
     * @param outputStream
     * @param eventMessage
     * @param responseMessage
     */
    private void responseTextMessage(ServletOutputStream outputStream, WxEventMessage eventMessage, String responseMessage) {
        //获取回复内容
        logger.info("Wechat response message:" + responseMessage);
        //创建回复
        XMLMessage xmlTextMessage = new XMLTextMessage(
                eventMessage.getFromUserName(),
                eventMessage.getToUserName(),
                responseMessage);
        //回复
        xmlTextMessage.outputStreamWrite(outputStream);
    }

    /**
     * 被动回复 图像,媒体消息
     *
     * @param outputStream
     * @param eventMessage
     * @param media_id
     */
    private void responseImageMessage(ServletOutputStream outputStream, WxEventMessage eventMessage, String media_id) {
        //创建回复
        XMLMessage xmlImageMessage = new XMLImageMessage(
                eventMessage.getFromUserName(),
                eventMessage.getToUserName(),
                media_id);
        //回复
        xmlImageMessage.outputStreamWrite(outputStream);
    }

    /**
     * 被动回复 图文消息
     *
     * @param outputStream
     * @param newsMessage
     */
    private void responseNewMessage(ServletOutputStream outputStream, WxEventMessage eventMessage, NewsMessage newsMessage) {
        List<XMLNewsMessage.Article> list = new ArrayList<>();
        if (newsMessage.getNews().getArticles() != null) {
            for (NewsMessage.Article a : newsMessage.getNews().getArticles()) {
                XMLNewsMessage.Article item = new XMLNewsMessage.Article();
                item.setTitle(a.getTitle());
                item.setDescription(a.getDescription());
                item.setUrl(a.getUrl());
                item.setPicurl(a.getPicurl());
                list.add(item);
            }
        }

        //创建回复
        XMLMessage xmlImageMessage = new XMLNewsMessage(
                eventMessage.getFromUserName(),
                eventMessage.getToUserName(),
                list);
        //回复
        xmlImageMessage.outputStreamWrite(outputStream);
    }

    /**
     * 数据流输出
     *
     * @param outputStream
     * @param text
     * @return
     */
    private boolean outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
