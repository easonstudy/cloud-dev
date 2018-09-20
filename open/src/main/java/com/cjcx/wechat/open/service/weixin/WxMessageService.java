package com.cjcx.wechat.open.service.weixin;


import com.cjcx.wechat.open.dto.UserDto;
import com.cjcx.wechat.open.entity.UserBalance;
import com.cjcx.wechat.open.object.ReceiptData;
import com.cjcx.wechat.open.object.WxEventMessage;
import com.cjcx.wechat.open.service.member.IMessageService;
import com.cjcx.wechat.open.service.member.ITransactionService;
import com.cjcx.wechat.open.service.member.IUserService;
import com.cjcx.wechat.open.utils.AESCrypt;
import com.cjcx.wechat.open.utils.JacksonUtil;
import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.weixin.TemplateMessageUtil;
import com.cjcx.wechat.open.web.ResultObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service
public class WxMessageService implements IWxMessageService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService userService;

    @Autowired
    IMessageService messageService;

    @Autowired
    ITransactionService transactionService;

    /**
     * 事件处理
     *
     * @param eventMessage
     * @return
     */
    @Override
    public Map<String, Object> eventMessageHandler(WxEventMessage eventMessage) {
        Map<String, Object> outmap = new HashMap<>();
        if (eventMessage == null) {
            outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
            outmap.put(WeixinUtil.RESPONSE_VALUE, "WxEventMessage null");
            return outmap;
        }

        try {
            String openId = eventMessage.getFromUserName();
            String event = eventMessage.getEvent();
            logger.info("处理事件:" + event);
            switch (event) {
                case WeixinUtil.EVENT_TYPE_SUBSCRIBE:
                    //关注
                    subscribe(eventMessage, outmap, event);
                    break;
                case WeixinUtil.EVENT_TYPE_UNSUBSCRIBE:
                    //取消关注
                    unsubcribe(eventMessage);
                    break;
                case WeixinUtil.EVENT_TYPE_CLICK:
                    String key = eventMessage.getEventKey();
                    logger.info("点击事件 key:" + key);
                    //网页授权 注册
                    if (WeixinUtil.EVENT_TYPE_REGISTER.equals(key)) {
                        outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.EVENT_TYPE_VIEW);
                        outmap.put(WeixinUtil.RESPONSE_VALUE, "注册测试");

                        //接口凭证
                        String access_token = WeixinUtil.getAccessToken();
                        //带 参数(注册:微信公众号OpenId)
                        String tag = "register" + WeixinUtil.EVENT_SEP + eventMessage.getFromUserName();
                        String url = WeixinUtil.URL_OAUTH2_AUTHORIZE_BASE.replace("[state]", tag);

                        //请求
                        String registerContent = RequestUtil.doGet(url, null);
                        logger.info("网页授权:" + registerContent);
                    } else {
                        customResponseMessage(openId, key, outmap);
                    }
                    break;
                case WeixinUtil.EVENT_TYPE_SCANCODE_PUSH:    //扫码后显示结果 或者(URL)
                case WeixinUtil.EVENT_TYPE_SCANCODE_WAITMSG: //扫码后跳回 公共号 被动回复消息
                    //扫码推事件
                    scancode(eventMessage, outmap);
                    break;
                case WeixinUtil.EVENT_TYPE_VIEW:
                    logger.info("view跳转url:" + eventMessage.getUrl());
                    break;
                default:
                    logger.info("未开发事件:" + event);
                    outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                    outmap.put(WeixinUtil.RESPONSE_VALUE, "未开发事件");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, ResultObject.ERRCODE_NO);
            outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
            outmap.put(WeixinUtil.RESPONSE_VALUE, "异常");
        }
        return outmap;
    }

    /**
     * 文字处理
     *
     * @param eventMessage
     * @return
     */
    @Override
    public Map<String, Object> textMessageHandler(WxEventMessage eventMessage) {
        Map<String, Object> outmap = new HashMap<>();
        String openId = eventMessage.getFromUserName();
        String content = eventMessage.getContent();

        customResponseMessage(openId, content, outmap);
        return outmap;
    }


    /**
     * 关注 调用会员注册API
     *
     * @param eventMessage
     * @param outmap
     * @param event
     */
    private void subscribe(WxEventMessage eventMessage, Map<String, Object> outmap, String event) {
        ResultObject result = userService.register(eventMessage);
        outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
        if(result.isOk()){
            outmap.put(ResultObject.PARAM_ERRCODE, ResultObject.ERRCODE_OK);
            outmap.put(WeixinUtil.RESPONSE_VALUE, WeixinUtil.resMap.get(event));
        } else {
            outmap.put(ResultObject.PARAM_ERRCODE, ResultObject.ERRCODE_OK);
            outmap.put(WeixinUtil.RESPONSE_VALUE, "注册失败");
        }
    }

    /**
     * 取消关注
     *
     * @param eventMessage
     */
    private void unsubcribe(WxEventMessage eventMessage) {
        //openId
        userService.unbind(eventMessage);
    }


    /**
     * 扫码推事件
     * ToUserName	开发者 微信号
     * FromUserName	发送方帐号（一个OpenID）
     * CreateTime	消息创建时间（整型）
     * MsgType	消息类型，event
     * Event	事件类型，scancode_push
     * EventKey	事件KEY值，由开发者在创建菜单时设定
     * ScanCodeInfo	扫描信息
     * ScanType	扫描类型，一般是qrcode
     * ScanResult	扫描结果，即二维码对应的字符串信息
     *
     * @param eventMessage
     */
    private void scancode(WxEventMessage eventMessage, Map<String, Object> outmap) throws Exception {
        String key = eventMessage.getEventKey();
        logger.info("扫码推事件 EventKey:" + key);
        if (WeixinUtil.SCANCODE_PUSH_KEY_ADD_SCORE.equals(key)) {
            String openId = eventMessage.getFromUserName();
            String receiptId = eventMessage.getScanCodeInfo().getScanResult();

            //ScanCodeInfo scanCodeInfo = eventMessage.getScanCodeInfo();
            logger.info("ScanCodeInfo:" + eventMessage.getScanCodeInfo().toString());

            //提交至 积分系统 处理小票交易
            ResultObject result = transactionService.submitTransactionToScoreSystem(openId, receiptId);
            //成功
            if(result.isOk()){
                JSONObject object = new JSONObject(result);
                ReceiptData receipt = JacksonUtil.json2Bean(object.get("data").toString(), ReceiptData.class);
                logger.info("通过小票ID查询小票数据:" + receipt.toString());

                String resultScore = result.getMsg().toString();
                Double points = Double.parseDouble(result.get("amount").toString());
                TemplateMessageUtil.messageTransaction(openId, points, resultScore, true);
                logger.info("openId:" + openId + ", receiptId:" + receiptId + "积分兑换成功");

                //以文本消息回复小票内容
                outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                StringBuffer buff = new StringBuffer();
                buff.append("商家:").append(receipt.getShopName()).append("\n");
                buff.append("单号:").append(receipt.getReceiptNum()).append("\n");
                buff.append("金额:").append(receipt.getTotalPrice()).append("\n");
                buff.append("支付方式:").append(receipt.getPayFlag()).append("\n");
                buff.append("抓取时间:").append(receipt.getGraspingTime()).append("\n");
                if (receipt.getImageurl() != null) {
                    buff.append("小票图片:").append("<a href='").append(receipt.getImageurl()).append("'>点击查看</a>").append("\n");
                }
                outmap.put(WeixinUtil.RESPONSE_VALUE, buff.toString());

            //异常 文本消息回复
            } else {
                //兑换异常
                String errorCode = result.getErrCode();
                String errorMsg = result.getMsg().toString();
                TemplateMessageUtil.messageTransaction(openId, 0.0, errorCode + ", 原因:" + errorMsg, false);
                logger.info("openId:" + openId + ", receiptId:" + receiptId + "积分兑换失败 错误码:" + errorCode + ", 原因:" + errorMsg);

                //outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                //outmap.put(WeixinUtil.RESPONSE_VALUE, "获取小票异常, 错误码:" + result.getErrCode() + ", 原因:" + result.getMsg());
                return;
            }
        }
    }


    /**
     * 自定义 被动回复消息
     */
    private void customResponseMessage(String openId, String key, Map outmap) {

        switch (key) {
            //生成演示二维码
            case "scoreImage":
                outmap.put(WeixinUtil.RESPONSE_TYPE, "scoreImage");

                String[] receiptArr = {"104", "108", "389", "527", "561"};
                String receiptAmount = receiptArr[(int) (Math.random() * 5)];

                double price = Double.parseDouble(receiptAmount);
                //返回小票标识
                String receiptId = (int) (Math.random() * 1000) + "_" + receiptAmount;
                outmap.put(WeixinUtil.RESPONSE_VALUE, receiptId);
                break;
            //个人信息 - 查询
            case "profile":
                UserDto userDto = userService.getUserByLoginName(openId);
                StringBuffer res = new StringBuffer();
                if (userDto == null) {
                    res.append("系统未找到该用户,请重新关注公众号.");
                } else {
                    //积分
                    BigDecimal socre = new BigDecimal(0);
                    UserBalance userBalance = userDto.getUserBalance();
                    if (userBalance != null && userBalance.getScore() != null) {
                        socre = userBalance.getScore();
                    }
                    res.append("昵称:").append(userDto.getNickName() == null ? "" : userDto.getNickName())
                            .append("\n积分:").append(socre)
                            .append("\n电话:").append(userDto.getPhone() == null ? "" : userDto.getPhone())
                            .append("\n邮箱:").append(userDto.getEmail() == null ? "" : userDto.getEmail());
                }
                outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                outmap.put(WeixinUtil.RESPONSE_VALUE, res.toString());
                break;
            //申请测试
            case "request":
                outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                String reqHrefkey = openId + ":" + System.currentTimeMillis();
                try {
                    reqHrefkey = AESCrypt.encrypt(reqHrefkey);
                    //URLEncoder
                    reqHrefkey = URLEncoder.encode(reqHrefkey, "UTF-8");
                    logger.debug("申请测试资料 密钥:" + reqHrefkey);

                    StringBuffer buff = new StringBuffer();
                    buff.append("请点击 <a href='http://android-pos.cn/#/user/request/")
                            .append(reqHrefkey)
                            .append("'>申请测试</a>")
                            .append("\n请准确填写申请联系方式, 我们会在24小时之内回复您, 谢谢支持.");

                    outmap.put(WeixinUtil.RESPONSE_VALUE, buff.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("申请测试异常: " + e.getMessage());
                }
                break;
            //完善资料
            case "perfect":
                outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                String hrefkey = openId + ":" + System.currentTimeMillis();
                try {
                    hrefkey = AESCrypt.encrypt(hrefkey);
                    //URLEncoder
                    hrefkey = URLEncoder.encode(hrefkey, "UTF-8");
                    logger.info("完善个人资料 密钥:" + hrefkey);
                    outmap.put(WeixinUtil.RESPONSE_VALUE, "请点击 <a href='http://android-pos.cn/#/user/perfect/" + hrefkey + "'>完善个人资料</a>");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("完善资料: " + e.getMessage());
                }
                break;
            //最新文章 历史
            case "history":
                outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.RESP_MESSAGE_TYPE_NEWS);
                //outmap.put(WeixinUtil.RESPONSE_VALUE, getNewMessage(openId));
                break;
            //自定义回复
            default:
                logger.info("未知命令");
                /*MessageDto messageDto = new MessageDto();
                messageDto.setMsgtype("reply");
                messageDto.setState(0);
                List<MessageDto> msglist = messageService.findByCondition(messageDto);
                Map replymap = new HashMap<>();
                msglist.forEach((MessageDto dto) -> {
                    replymap.put(dto.getReplyKey(), dto.getContent());
                });
                if (replymap.get(key) != null) {
                    outmap.put(WeixinUtil.RESPONSE_TYPE, WeixinUtil.REQ_MESSAGE_TYPE_TEXT);
                    outmap.put(WeixinUtil.RESPONSE_VALUE, replymap.get(key));
                }*/
                break;
        }

    }


    /**
     * 最新 图文消息

    private NewsMessage getNewMessage(String openId) {
        List<NewsMessage.Article> list = new ArrayList<>();

        MessageDto messageDto = new MessageDto();
        messageDto.setMsgtype("news");
        messageDto.setState(0);
        List<MessageDto> msglist = messageService.findByCondition(messageDto);

        for (int i = 0; i < msglist.size(); i++) {
            messageDto = msglist.get(i);
            NewsMessage.Article article = new NewsMessage.Article(messageDto.getTitle(), messageDto.getDescription(), messageDto.getUrl(), messageDto.getPicurl());
            list.add(article);

            //最多8条
            if (i >= 7) {
                break;
            }
        }

        if (msglist.size() == 0) {
            String title = "测试消息";
            String description = "请配置消息数据";
            String url = "http://mp.weixin.qq.com/s/FzyvghqOWx_p60juj-7kVg";
            String picurl = "https://mmbiz.qpic.cn/mmbiz_jpg/ncGZvVc5rL3ADwSFCqiagKsksG8n5H8jTQdIHDxMOxzxlRYOx1oBze1yBU9g83CcE50zyr5EGvF1Iblw8fr4FcA/0?wx_fmt=jpeg";
            NewsMessage.Article article = new NewsMessage.Article(title, description, url, picurl);
            list.add(article);
        }
        NewsMessage newsMessage = new NewsMessage(openId, list);
        return newsMessage;
    } */

}
