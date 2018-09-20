package com.cjcx.aiserver.ai.baidu;

import com.cjcx.aiserver.ai.AbstractAccount;
import com.cjcx.aiserver.ai.AbstractAccountManager;
import com.cjcx.aiserver.ai.AccountType;
import com.cjcx.aiserver.ai.App;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BaiduAccountManager extends AbstractAccountManager {

    /**
     * key  :   apiKey
     * value:   secretKey
     */
    private static Map<String, AbstractAccount> accountMap = new HashMap<>();

    /**
     * 请求的最大次数限制
     */
    private static Map<String, Map<String, Integer>> limitMap = new HashMap<>();



    public BaiduAccountManager() {
        log.info("加载百度账户");
        init();
    }


    /**
     * 配置账户
     */
    private void init() {
        /**
         * 图形OCR账户
         */
        Account ocr_user_1 = new Account(App.IMAGE, "11324883", "jSD6dK51ecVsIlSZyGQM06Se", "ALXlRR5XEWH51X9qF9WlIgFVZ27GiQab", Boolean.TRUE, "老郭");
        accountMap.put(ocr_user_1.getApiKey(), ocr_user_1);
        limitMap.put(ocr_user_1.getApiKey(), limitConfig());

        Account ocr_user_2 = new Account(App.IMAGE, "11638249", "AlwTUYbPtpbQHKxPzXlNGRKj", "PRvKE3VWPLdrNNtOIehuMWbFLxUAzpTq", Boolean.TRUE, "马哥");
        accountMap.put(ocr_user_2.getApiKey(), ocr_user_2);
        limitMap.put(ocr_user_2.getApiKey(), limitConfig());

        Account face_user_1 = new Account(App.FACE, "11642068", "gcOVC13FSipvYn4aVw4hkYlS", "hTPAPwTQDs6xF6ytmVzOfx5bCKsXKt9u", Boolean.TRUE, "老郭");
        accountMap.put(face_user_1.getApiKey(), face_user_1);
    }


    private static Map<String, Integer> limitConfig(){
        Map<String, Integer> limit = new HashMap<>();
        limit.put(BaiduInterface.general_basic, 50000);
        limit.put(BaiduInterface.url_accurate_basic, 500);
        limit.put(BaiduInterface.url_accurate, 50);
        return limit;
    }

    /**
     * 获取 access_token
     * @param app  枚举App.FACE , App.IMAGE
     * @return
     */
    public static String getDefaultAccessToken(App app){
        return BaiduTokenManager.getToken(getDefaultApiKey(app));
    }

    /**
     * 获取 图像识别  apiKey
     *
     * @return
     */
    public static String getImageDefaultApiKey() {
        return getDefaultApiKey(App.IMAGE);
    }

    /**
     * 获取 人脸识别 apiKey
     *
     * @return
     */
    public static String getFaceDefaultApiKey() {
        return getDefaultApiKey(App.FACE);
    }


    /**
     * 公共 access_token
     * @param app
     * @return
     */
    private static String getDefaultApiKey(App app) {
        Account account = null;
        for (Map.Entry<String, AbstractAccount> o : accountMap.entrySet()) {
            if(o.getValue().getAccountType() == AccountType.BAIDU) {
                account = (Account)o.getValue();
                if (account.getApp().equals(app) && account.getIsUsed()) {
                    return account.getApiKey();
                }
            }
        }
        return null;
    }

    @Override
    public Map<String, AbstractAccount> getAccountMap() {
        return accountMap;
    }

    @Override
    public Map<String, Map<String, Integer>> getLimitMap() {
        return limitMap;
    }

    @NoArgsConstructor
    @Data
    @ToString(callSuper = true)
    public static class Account extends AbstractAccount {

        private String appId;
        private String apiKey;
        private String secretKey;

        public Account(App app, String appId, String apiKey, String secretKey, Boolean isUsed, String remark){
            super.app = app;
            this.appId = appId;
            this.apiKey = apiKey;
            this.secretKey = secretKey;
            super.isUsed = isUsed;
            super.remark = remark;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.BAIDU;
        }
    }
}
