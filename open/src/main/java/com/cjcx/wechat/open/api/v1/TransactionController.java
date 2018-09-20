package com.cjcx.wechat.open.api.v1;


import com.cjcx.wechat.open.service.member.ITransactionService;
import com.cjcx.wechat.open.weixin.TemplateMessageUtil;
import com.cjcx.wechat.open.web.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ITransactionService transactionService;

    /**
     * 积分兑换
     *
     * @param openId
     * @param receiptId
     * @return
     */
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public String score(@RequestParam("openId") String openId, @RequestParam("receiptId") String receiptId) {
        Map<String, Object> map = null;
        try {
            logger.info("公众号 openId:" + openId + "receiptId:" + receiptId);
            ResultObject r = null;
            //测试-创建交易信息
            map = transactionService.testdoTransactionByReceiptId(openId, receiptId);
            Integer errorCode = Integer.parseInt(map.get(ResultObject.PARAM_ERRCODE).toString());
            if (errorCode == 0) {
                //最新积分值
                String resultScore = map.get(ResultObject.PARAM_MSG).toString();
                Double points = Double.parseDouble(map.get("amount").toString());
                TemplateMessageUtil.messageTransaction(openId, points, resultScore, true);
                logger.info("测试- openId:" + openId + ", receiptId:" + receiptId + "积分兑换成功");
            } else {
                String reason = map.get(ResultObject.PARAM_MSG).toString();
                TemplateMessageUtil.messageTransaction(openId, 0.0, errorCode.toString(), false);
                logger.info("测试- openId:" + openId + ", receiptId:" + receiptId + "积分兑换失败 错误码:" + errorCode + ", 原因:" + reason);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "请放回公众号查看";
    }

}
