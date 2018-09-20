package com.cjcx.wechat.open.config;

public class WeixinConfig {
    public static final String APPID = "wx621a5c621c43da89";
    public static final String APPSECRET = "37fecf7d0ed2b80febf8d369215fd64d";
    public static final String token = "cjcx";
    public static final String EncodingAESKey = "jQn7V7XFkU7ijv94heYJb88JBDyORHSbTbMcaVRf4QI";

    protected static final String BASE_URI = "https://api.weixin.qq.com";
    protected static final String MEDIA_URI = "http://file.api.weixin.qq.com";
    protected static final String MP_URI = "https://mp.weixin.qq.com";
    protected static final String MCH_URI = "https://api.mch.weixin.qq.com";
    protected static final String OPEN_URI = "https://open.weixin.qq.com";

    /**
     * 获取access_token
     */
    public static String URL_ACCESS_TOKEN = BASE_URI + "/cgi-bin/token?grant_type=client_credential&appid=" + WeixinConfig.APPID + "&secret=" + WeixinConfig.APPSECRET;
    /**
     * 创建二维码
     */
    protected static final String URL_QRCODE_CREATE = BASE_URI + "/cgi-bin/qrcode/create?access_token=";
    protected static final String URL_SHOWQRCODE = MP_URI + "/cgi-bin/showqrcode?ticket=";
    /**
     * 网页授权 回调地址
     */
    protected static final String URL_AUTHORIZE = "http://api.android-pos.cn/wx/authorize";
    /**
     * 网页授权
     */
    public static final String URL_OAUTH2_AUTHORIZE_BASE = OPEN_URI + "/connect/oauth2/authorize?appid=" + WeixinConfig.APPID + "&redirect_uri=" + URL_AUTHORIZE + "&response_type=code&scope=snsapi_base&state=[state]#wechat_redirect";
    public static final String URL_OAUTH2_AUTHORIZE_USERINFO = OPEN_URI + "/connect/oauth2/authorize?appid=" + WeixinConfig.APPID + "&redirect_uri=" + URL_AUTHORIZE + "&response_type=code&scope=snsapi_userinfo&state=[state]#wechat_redirect";
    /**
     * 请求用户信息
     */
    public static final String URL_REQUEST_USERINFO = BASE_URI + "/sns/userinfo?access_token=[access_token]&openid=[open_id]";

}
