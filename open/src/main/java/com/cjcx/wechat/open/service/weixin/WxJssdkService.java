package com.cjcx.wechat.open.service.weixin;

import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.web.ResultObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;

@Service
public class WxJssdkService implements IWxJssdkService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResultObject getJssdkConfig(String url) {
        ResultObject result = new ResultObject();
        try {
            //生成签名的随机串
            String nonceStr = create_nonce_str();
            //生成签名的时间戳
            String timestamp = create_timestamp();
            //签名
            String signature = createSignature(url, nonceStr, timestamp);

            result.put("appId", WeixinUtil.APPID);
            result.put("timestamp", timestamp);
            result.put("nonceStr", nonceStr);
            result.put("signature", signature);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("wrong:" + e.getMessage());
            result.setErrCode("-1");
        }
        return result;
    }

    private String getJsapiTicket() {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + WeixinUtil.getAccessToken() + "&type=jsapi";
        try {
            String rs = RequestUtil.doGet(url, null);
            JSONObject obj_content = new JSONObject(rs);
            String jsapi_ticket = obj_content.getString("ticket");
            return jsapi_ticket;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取微信jsapiTicket错误.");
        }
        return null;
    }

    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    private String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 根据jsapi_ticket等参数进行SHA1加密
     *
     * @param url 当前页面url
     */
    public String createSignature(String url, String nonceStr, String timestamp) {
        String jsapiTicket = getJsapiTicket();
        String signature = "jsapi_ticket=" + jsapiTicket;
        signature += "&noncestr=" + nonceStr;
        signature += "&timestamp=" + timestamp;
        signature += "&url=" + url;

        logger.info("jsapi_ticket:{}", jsapiTicket);
        logger.info("noncestr:{}", nonceStr);
        logger.info("timestamp:{}", timestamp);
        logger.info("url:{}", url);
        logger.info("signture before:{}", signature);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signature.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());

            logger.info("signture after:" + signature);
        } catch (Exception e) {
            logger.error("Signature for SHA-1 is error:{}", e);
        }


        return signature;
    }
}
