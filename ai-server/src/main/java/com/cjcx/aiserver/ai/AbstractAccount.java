package com.cjcx.aiserver.ai;

import lombok.Data;

@Data
public abstract class AbstractAccount {

    protected App app = App.IMAGE;

    protected Boolean isUsed = Boolean.TRUE;

    protected String remark;

    public abstract AccountType getAccountType();
}
