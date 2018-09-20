package com.cjcx.wechat.open.service.member;

import com.cjcx.wechat.open.config.MemberConfig;
import com.cjcx.wechat.open.utils.JacksonUtil;
import com.cjcx.wechat.open.utils.RequestUtil;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.web.ResultObject;
import com.cjcx.wechat.open.weixin.SnsUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weixin.popular.bean.sns.SnsToken;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService implements ITransactionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 网页授权
     *
     * @param code  用来兑换微信用户信息
     * @param state 参数标识
     * @return
     */
    public ResultObject handleAuthorize(String code, String state) {
        ResultObject outmap = new ResultObject();
        try {
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
                logger.info("Get code or receiptId is null");
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                outmap.put(ResultObject.PARAM_MSG, "参数code或state为空");
                return outmap;
            }
            logger.info("参数数据 - code:" + code + ", 标识state:" + state);

            SnsToken snsToken = null;
            try {
                snsToken = SnsUtil.getAccessTokenByCode(code);
            } catch (Exception e) {
                e.printStackTrace();
                outmap.put(ResultObject.PARAM_ERRCODE, -2);
                outmap.put(ResultObject.PARAM_MSG, "微信code换取access_token错误");
                return outmap;
            }

            String access_token = snsToken.getAccess_token();
            String openid = snsToken.getOpenid();
            logger.info("微信数据 - 授权OpenId:" + openid + ", Access_token:" + access_token);

            if (state.contains(WeixinUtil.EVENT_TYPE_REGISTER)) {
                String[] arr = state.split(WeixinUtil.EVENT_SEP);
                String pubOpenId = arr[1];
                logger.info("注册数据 - 公众号OpenId:" + pubOpenId + ", 授权OpenId:" + openid);
                //自助积分
            } else if (state.contains("app_score_self")) {
                String[] arr = state.split(WeixinUtil.EVENT_SEP);
                String phone = arr[1];
                outmap.put("phone", phone);
                outmap.put("openId", openid);
                logger.info("自助积分数据 - 电话:" + phone + ", 授权OpenId:" + openid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outmap;
    }

    @Override
    public Map<String, Object> testdoTransactionByReceiptId(String openId, String receiptId) {
        Map<String, Object> map = new HashMap<>();
        try {
            String params = "openId=" + openId + "&receiptId=" + receiptId;
            //logger.info("测试积分请求参数:{}" , params);
            String result = RequestUtil.doGet(MemberConfig.url_test_transaction_socre, params);
            logger.info("测试积分兑换结果:{}", result);
            map = JacksonUtil.json2Bean(result, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public ResultObject submitTransactionToScoreSystem(String openId, String receiptId) {
        ResultObject r = new ResultObject();
        try {
            String params = "openId=" + openId + "&receiptId=" + receiptId;
            //logger.info("测试积分请求参数:{}" , params);
            String result = RequestUtil.doPost(MemberConfig.url_transaction_score, params);
            logger.info("扫码积分结果:{}", result);
            r = JacksonUtil.json2Bean(result, ResultObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
