package com.cjcx.aiserver.cache;

import com.cjcx.aiserver.ai.AbstractAccount;
import com.cjcx.aiserver.ai.App;
import com.cjcx.aiserver.ai.baidu.BaiduAccountManager;
import com.cjcx.aiserver.ai.baidu.BaiduTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class CacheHolder {

    //访问的次数限制
    private static ConcurrentMap<String, ConcurrentMap<String, Integer>> limitTimesMap = new ConcurrentHashMap<>();

    /**
     * 是否是测试环境
     */
    @Value("${ai.baidu.test}")
    private Boolean aiBaiduIsDev;

    @Autowired
    BaiduAccountManager baiduAccountManager;

    /**
     * 容器启动后执行
     */
    public void start() {
        baiduAccountInitalizer();
    }

    /**
     * 百度access_token处理
     */
    private void baiduAccountInitalizer() {
        BaiduTokenManager.setDaemon(false);
        log.info("百度当前为{}环境", aiBaiduIsDev ? "测试" : "正式");

        for (Map.Entry<String, AbstractAccount> o : baiduAccountManager.getAccountMap().entrySet()) {
            AbstractAccount abstractAccount = o.getValue();
            BaiduAccountManager.Account account = (BaiduAccountManager.Account) abstractAccount;
            log.info("{}账户激活: {}, 信息:{}", account.getAccountType(), account.getIsUsed(), account.toString());
            if (account.getIsUsed()) {
                if (aiBaiduIsDev) {
                    //测试环境
                    if (account.getApp().equals(App.IMAGE)) {
                        //BaiduTokenManager.setToken(account.getApiKey(), (int) (Math.random() * 10000) + "");
                        if (!"jSD6dK51ecVsIlSZyGQM06Se".equals(account.getApiKey())) {
                            baiduAccountManager.stopAccountByKey(account.getApiKey());
                        } else {
                            BaiduTokenManager.setToken("jSD6dK51ecVsIlSZyGQM06Se", "24.0621a72ada8cc372c86558c97b773e03.2592000.1539976299.282335-11638249");
                        }
                    } else if (account.getApp().equals(App.FACE)) {
                        BaiduTokenManager.setToken("gcOVC13FSipvYn4aVw4hkYlS", "24.38a9a26d8b3e810089d9ff6e8d3cacfe.2592000.1539976300.282335-11642068");
                    }
                } else {
                    //生产环境
                    BaiduTokenManager.init(account.getApiKey(), account.getSecretKey());
                }
            }
        }
        log.info("百度 默认图像access_token: " + BaiduTokenManager.getToken(baiduAccountManager.getImageDefaultApiKey()));
        log.info("百度 默认人脸access_token: " + BaiduTokenManager.getToken(baiduAccountManager.getFaceDefaultApiKey()));
    }


    /**
     * 百度 每天凌晨0点 清理请求次数限制
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void restartBaiduLimitTask() {
        synchronized (this) {
            log.info("凌晨0点 清理百度限制");
            limitTimesMap.clear();
            //启动已停用账户
            baiduAccountManager.startAccount();
        }
    }


    public ConcurrentMap<String, ConcurrentMap<String, Integer>> getLimitTimesMap() {
        return limitTimesMap;
    }
}
