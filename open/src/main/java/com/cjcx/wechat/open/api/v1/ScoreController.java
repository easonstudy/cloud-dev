package com.cjcx.wechat.open.api.v1;


import com.cjcx.wechat.open.service.weixin.IWxJssdkService;
import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.web.ResultObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ScoreController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IWxJssdkService wxJssdkService;

    /**
     * jssdk  获取初始配置
     * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/jssdk", method = RequestMethod.POST)
    public ResultObject jssdk(@RequestParam("url") String url) {
        logger.info("Jssdk got url:" + url);
        ResultObject result = new ResultObject();
        result = wxJssdkService.getJssdkConfig(url);
        return result;
    }

    /**
     * 临时图片上传的serverId(获取微信图片的meadi_id)
     *
     * @param serverId
     * @return
     */
    @RequestMapping(value = "/images/url")
    public ResultObject uploadImage(@RequestParam("serverId") String serverId,
                                    @RequestParam(value = "local", required = false) Integer local) {
        ResultObject result = new ResultObject();

        try {
            logger.info("Got serverId:{}", serverId);
            String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + WeixinUtil.getAccessToken() + "&media_id=" + serverId;
            logger.info("临时图片地址:{}", url);

            Map<String, Object> map = new HashMap<>();
            map.put("url", URLEncoder.encode(url, "UTF-8"));
            map.put("local", local);
            String params = RequestUtil.getUrlParamsByMap(map);
            logger.info("params:{}", params);
            //存储到图片服务器 返回新图片的地址
            String imgResult = RequestUtil.doGet("http://120.79.210.194:8084/images/receipt_audit", params);
            logger.info("图片服务器返回结果:{}", imgResult);

            JSONObject json = new JSONObject(imgResult);
            String errorCode = json.getString("errorCode");
            String errorMsg = json.getString("errorMsg");

            result.setErrCode(errorCode);
            result.setData(errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrCode("-1");
            result.setMsg("异常");
        }
        return result;
    }

}
