package com.cjcx.wechat.open.service.member;

import com.cjcx.wechat.open.web.ResultObject;

import java.util.Map;

public interface ITransactionService {

    ResultObject handleAuthorize(String code, String state);

    Map<String, Object> testdoTransactionByReceiptId(String openId, String receiptId);

    ResultObject submitTransactionToScoreSystem(String openId, String receiptId);
}
