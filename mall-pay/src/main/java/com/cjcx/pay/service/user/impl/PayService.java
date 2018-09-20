package com.cjcx.pay.service.user.impl;

import com.cjcx.pay.cache.CacheHolder;
import com.cjcx.pay.dto.SystemParamDto;
import com.cjcx.pay.dto.TransactionDto;
import com.cjcx.pay.entity.SystemParam;
import com.cjcx.pay.service.user.IPayService;
import com.cjcx.pay.service.user.ITransactionService;
import com.cjcx.pay.utils.HttptUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
@Service
public class PayService implements IPayService {


    @Autowired
    CacheHolder cacheHolder;

    @Autowired
    ITransactionService iTransactionService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async("taskAsync")
    public Future<Map<String, Object>> submitToThirdPay(TransactionDto dto) {
        Map<String, Object> map = new HashMap<>();
        try {
            SystemParamDto systemParamDto = cacheHolder.getSystemParamMap().get(SystemParam.KEY_MERCHANT_NUMBER);

            log.info("async method start, " + Thread.currentThread().getName());
            Thread.sleep(2000);

            String url = "http://hypaytest.ccc360.com/hypay_rest/singleConf";

            Map<String, Object> params = new HashMap<>();
            //微信商户
            //params.put("mchNo", "1321866001");
            params.put("mchNo", systemParamDto.getParamValue());
            //商品备注标签
            params.put("goodsTag", dto.getRemark());
            //商品价格
            params.put("price", dto.getAmount().intValue());
            //微信条码
            //params.put("goodsId", "6906791582555");
            params.put("goodsId", dto.getBarcode());

            String response = HttptUtils.doPost(url, params);

            //log.info("请求支付返回结果:{}" + response);
            JSONObject json = new JSONObject(response);
            String stateCode = json.getString("stateCode");
            String stateMessage = json.getString("stateMessage");
            log.info("返回消息 code:{}, message:{}", stateCode, stateMessage);
            if ("00".equals(stateCode)) {
                map.put("code", 0);
                //成功
                dto.setState(1);
            } else {
                //失败
                dto.setState(2);
            }
            dto.setRemark(stateMessage);
            dto.setUpdateBy("支付接口调用");
            iTransactionService.updateById(dto);
            log.info("async method end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<Map<String, Object>>(map);
    }
}
