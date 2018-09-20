package com.cjcx.aiserver.ai.tencent;

import com.cjcx.aiserver.ai.AbstractAccount;
import com.cjcx.aiserver.ai.AbstractAccountManager;
import com.cjcx.aiserver.ai.AccountType;
import com.cjcx.aiserver.ai.App;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Inherited;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TencentAccountManager extends AbstractAccountManager {

    /**
     * key  :   apiKey
     * value:   secretKey
     */
    private static Map<String, AbstractAccount> accountMap = new HashMap<>();

    /**
     * 请求的最大次数限制
     */
    private static Map<String, Map<String, Integer>> limitMap = new HashMap<>();

    public TencentAccountManager() {
        log.info("加载腾讯云账户");
        init();
    }

    public void init() {
        Account a1 = new Account(App.IMAGE, 1253763720L, "AKIDKYionbviz3vhGgSHUbaNCuWyFWdgmMyO", "T7cx75r02tY2PVvJVVL1W1LhrqiPSp7E", Boolean.TRUE, "老郭");
        accountMap.put(a1.getSecretId(), a1);

        Map<String, Integer> limit1 = new HashMap<>();
        limit1.put(TencentConstant.url_general, 30);
        limitMap.put(a1.getSecretId(), limit1);
    }

    /**
     * 获取 图像识别默认access_token
     *
     * @return
     */
    public static Account getImageDefaultAccount() {
        try {
            return getDefaultAccount(App.IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Account getDefaultAccount(App app) throws Exception {
        Account account = null;
        for (Map.Entry<String, AbstractAccount> o : accountMap.entrySet()) {
            account = (Account) o.getValue();
            if (account.getApp().equals(app) && account.getIsUsed()) {
                return account;
            }
        }
        return null;
    }


    @Override
    protected Map<String, AbstractAccount> getAccountMap() {
        return accountMap;
    }

    @Override
    protected Map<String, Map<String, Integer>> getLimitMap() {
        return limitMap;
    }

    @NoArgsConstructor
    @Data
    @ToString(callSuper = true)
    public static class Account extends AbstractAccount {
        private Long appId;
        private String secretId;
        private String secretKey;

        public Account(App app, Long appId, String secretId, String secretKey, Boolean isUsed, String remark){
            super.app = app;
            this.appId = appId;
            this.secretId = secretId;
            this.secretKey = secretKey;
            super.isUsed = isUsed;
            super.remark = remark;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.TENCENT;
        }
    }
}
