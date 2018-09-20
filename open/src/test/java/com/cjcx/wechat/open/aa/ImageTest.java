package com.cjcx.wechat.open.aa;

import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import org.junit.Test;

public class ImageTest {

    @Test
    public void aa(){
        String serverId = "tSG2m6-LEdDSoTOWZv_Jx9ArsOkUNlnJ-4XMDYY8www8aWZSJOTfR4qRnBJyX_X9";
        String token = "10_PXcXa2ToHLrR7EVH1jWE7wn8TdeDpW6XVIPfIsZeoFW_2GnL51VjzDLGRjMey6dF21_3j1qUBORstLseYVajcN6xZqGb-W9jE01VA-rMphJs0FvMP23h569b9lR6VSX4FAFAndSwoEilb26zVQNfABAGBY";

        String url ="https://api.weixin.qq.com/cgi-bin/media/get?access_token="+ token+"&media_id="+serverId;
        System.out.println(url);
        /*String wxResult = null;
        try {
            wxResult = RequestUtil.doGet(url, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(wxResult);*/
    }
}
