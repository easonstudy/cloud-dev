package com.cjcx.member.service.user;

import com.cjcx.member.dto.TransactionDto;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.ReceiptData;

import java.util.Map;

public interface ITransactionService extends IBaseService<TransactionDto, Integer> {

    Map<String, Object> getReceiptDataById(String receiptId);

    ResultObject doTransactionByReceiptId(String userTag, String receiptId);
    ResultObject doTransactionByReceiptId(String userTag, String receiptId, ReceiptData receiptData);

    Map<String, Object> testdoTransactionByReceiptId(String openId, String receiptId, double price);

    Map<String, Object> searchTransaction(String openId, String receiptId);

    TransactionDto getTransactionRecord(String openId, String receiptId);

}
