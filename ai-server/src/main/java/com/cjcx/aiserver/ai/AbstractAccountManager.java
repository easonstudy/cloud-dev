package com.cjcx.aiserver.ai;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 账户 公共管理内
 */
@Slf4j
public abstract class AbstractAccountManager {

    /**
     * 停用账户
     *
     * @param apiKey
     * @return
     */
    public Boolean stopAccountByKey(String apiKey) {
        for (Map.Entry<String, AbstractAccount> o : getAccountMap().entrySet()) {
            if (o.getKey().equals(apiKey)) {
                o.getValue().setIsUsed(Boolean.FALSE);
                log.info("停用该账户:{}", o.getValue());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 启用账户
     *
     * @return
     */
    public Boolean startAccount() {
        for (Map.Entry<String, AbstractAccount> o : getAccountMap().entrySet()) {
            if (!o.getValue().getIsUsed()) {
                o.getValue().setIsUsed(Boolean.TRUE);
                log.info("启动 {}", o.getValue().toString());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 获取 账户限制条件
     *
     * @param apiKey
     * @return
     */
    public Map<String, Integer> getAccountLimitByKey(String apiKey) {
        return getLimitMap().get(apiKey);
    }

    /**
     * 获取 账户集合
     *
     * @return
     */
    protected abstract Map<String, AbstractAccount> getAccountMap();


    protected abstract Map<String, Map<String, Integer>> getLimitMap();
}
