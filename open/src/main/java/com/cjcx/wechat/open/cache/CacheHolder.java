package com.cjcx.wechat.open.cache;

import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.weixin.MenuUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import weixin.popular.api.MenuAPI;
import weixin.popular.bean.BaseResult;

@Configuration
public class CacheHolder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void start(long period) {
        /**
         * 微信自定义菜单 - access_token 不为空
         */
        if (!StringUtils.isEmpty(WeixinUtil.getAccessToken())) {
            BaseResult result = MenuAPI.menuCreate(WeixinUtil.getAccessToken(), MenuUtil.initMenuButtons());
            if ("0".equals(result.getErrcode())) {
                logger.info("微信自定义菜单 创建成功");
            } else {
                logger.info("微信自定义菜单 创建失败 代码:" + result.getErrcode() + ", 消息:" + result.getErrmsg());
            }
        }
    }

}
