package com.cjcx.wechat.open.listener;

import com.cjcx.wechat.open.config.WeixinConfig;
import com.cjcx.wechat.open.utils.IpUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.weixin.JssdkManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import weixin.popular.support.TokenManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Configuration
public class TokenManagerListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(TokenManagerListener.class);

    private static String mac = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WEB容器 初始化=======================>Start");
        try {
            //TokenManager.setDaemon(false);
            //TokenManager.init(WeixinConfig.APPID, WeixinConfig.APPSECRET);

            String localMac = IpUtil.getLocalMac();
            if ("00-16-3E-04-A1-55".equals(localMac)) {
                logger.debug("服务端初始化 开启 微信AccessToken管理 ");
                TokenManager.setDaemon(false);
                TokenManager.init(WeixinConfig.APPID, WeixinConfig.APPSECRET);

                logger.debug("服务端初始化 开启 微信Jssdk Ticket管理 ");
                JssdkManager.setDaemon(false);
                JssdkManager.init(WeixinUtil.getAccessToken());
            } else {
                logger.debug("本地初始化 不开启 微信AccessToken管理 本地Mac:" + localMac);
            }

            this.mac = localMac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("WEB容器 初始化=======================>End");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("WEB容器  关闭=======================>Start");
        //TokenManager.destroyed();
        if ("00-16-3E-04-A1-55".equals(mac)) {
            logger.debug("服务端 销毁 微信Jssdk Ticket管理 ");
            JssdkManager.destroyed();

            logger.debug("服务端 销毁 微信AccessToken管理 ");
            TokenManager.destroyed();
        } else {
            logger.debug("本地销毁 本地Mac:" + mac);
        }
        logger.info("WEB容器  关闭=======================>End");
    }
}
