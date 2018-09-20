package com.cjcx.wechat.open.config;

/**
 * 对接调用-会员后台
 */
public class MemberConfig {

    public final static String url = "http://120.79.210.194:8083";

    public final static String url_register = url + "/api/v1/user/wx_subscribe";

    public final static String url_unbind = url + "/api/v1/user/wx_unsubscribe";

    public final static String url_user_info = url + "/api/v1/user/wx_userinfo";

    public final static String url_test_transaction_socre = url + "/api/v1/transaction/test/score";

    public final static String url_transaction_score = url + "/api/v1/transaction/score";
}
