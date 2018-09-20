package com.cjcx.wechat.open.weixin;


import com.cjcx.wechat.open.config.WeixinConfig;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

/**
 * 网页授权API
 */
public class SnsUtil extends WeixinUtil {

    //使用code换取access_token
    public static SnsToken getAccessTokenByCode(String code) throws Exception {
        return SnsAPI.oauth2AccessToken(WeixinConfig.APPID, WeixinConfig.APPSECRET, code);
    }




    //使用code换取access_token
    public static SnsToken getAccessTokenByCodeTest(String code) throws Exception {
        String content = null;
        try {
            content = "{"
                    + "\"access_token\": \"OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5AI1bw2uqN--2jXoBLIM5d6L9RImvm8Vg8cBAiLpWA8Vw\","
                    + "\"expires_in\": 7200,"
                    + "\"refresh_token\": \"OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5CZPAwZksiuz_6x_TfkLoXLU7kdKM2232WDXB3Msuzq1A\","
                    + "\"openid\": \"oLVPpjqs9BhvzwPj5A-vTYAX3GLc\","
                    + "\"scope\": \"snsapi_userinfo\""
                    + "}";
        } catch (Exception e) {
            e.printStackTrace();/*
            outmap.put(ResultObject.PARAM_ERRCODE, -2);
            outmap.put(ResultObject.PARAM_MSG, "微信code换取access_token错误");
            return outmap;*/
        }
        //提取access_token 和 OpenId
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SnsToken snsToken = mapper.readValue(content, SnsToken.class);
        return snsToken;
    }

}
