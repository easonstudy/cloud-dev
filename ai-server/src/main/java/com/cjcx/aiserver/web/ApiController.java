package com.cjcx.aiserver.web;

import com.cjcx.aiserver.ai.baidu.BaiduAccountManager;
import com.cjcx.aiserver.ai.baidu.BaiduTokenManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    /**
     * 帮助获取当前的access_token
     * @return
     */
    @RequestMapping("/api/baidu/token/image")
    public String getImageAccessToken(){
        return BaiduTokenManager.getToken(BaiduAccountManager.getImageDefaultApiKey());
    }

    /**
     * 帮助获取当前的access_token
     * @return
     */
    @RequestMapping("/api/baidu/token/face")
    public String getFaceAccessToken(){
        return BaiduTokenManager.getToken(BaiduAccountManager.getFaceDefaultApiKey());
    }

}
