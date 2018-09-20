package com.cjcx.pay.service.user;

import com.cjcx.pay.dto.TransactionDto;

import java.util.Map;
import java.util.concurrent.Future;

public interface IPayService {

    Future<Map<String, Object>> submitToThirdPay(TransactionDto dto);

}
