package com.cjcx.member.service.user.impl;

import com.cjcx.member.cache.CacheHolder;
import com.cjcx.member.dao.TransactionDao;
import com.cjcx.member.dto.SystemParamDto;
import com.cjcx.member.dto.TransactionDto;
import com.cjcx.member.dto.UserDto;
import com.cjcx.member.entity.SystemParam;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.ReceiptData;
import com.cjcx.member.service.user.ITransactionService;
import com.cjcx.member.service.user.IUserService;
import com.cjcx.member.utils.GsonUtil;
import com.cjcx.member.utils.RequestUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService extends BaseService<TransactionDto, Integer> implements ITransactionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CacheHolder cacheHolder;

    @Autowired
    TransactionDao transactionDao;

    @Override
    protected IBaseDao<TransactionDto, Integer> getDao() {
        return transactionDao;
    }

    @Autowired
    IUserService userService;

    /**
     * 网页授权
     *
     * @param code  用来兑换微信用户信息
     * @param state 参数标识
     * @return
     */
    @Transactional
    public Map<String, Object> handleAuthorize(String code, String state) {
        Map<String, Object> outmap = new HashMap<>();
        try {
            /*if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state)) {
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

*/

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outmap;
    }


    /**
     * 测试交易
     *
     * @param openId
     * @param receiptId
     * @param price
     * @return
     */
    @Override
    public Map<String, Object> testdoTransactionByReceiptId(String openId, String receiptId, double price) {
        Map<String, Object> outmap = new HashMap<>();
        try {
            //测试
            ReceiptData receipt = new ReceiptData();
            receipt.setTotalPrice(price);
            logger.debug("交易 - 小票测试数据");

            // 兑换积分
            Map<String, Object> map = new HashMap<>();
            map.put("userTag", openId);
            map.put("uniqueId", receiptId);
            map.put("amount", receipt.getTotalPrice());
            map.put("type", 1);
            map.put("insertMode", "SCAN QR CODE");
            map.put("createUser", openId);

            Map<String, Object> sp_map = this.doTransaction(map);
            Integer sp_errcode = Integer.parseInt(sp_map.get(ResultObject.PARAM_ERRCODE).toString());
            if (sp_errcode == -1) {
                outmap.put(ResultObject.PARAM_ERRCODE, -5);
                outmap.put(ResultObject.PARAM_MSG, "用户不存在.");
                return outmap;
            } else if (sp_errcode == -2) {
                outmap.put(ResultObject.PARAM_ERRCODE, -6);
                outmap.put(ResultObject.PARAM_MSG, "已兑换.");
                return outmap;
            }
            String sp_out = sp_map.get("value").toString();
            outmap.put(ResultObject.PARAM_ERRCODE, 0);
            outmap.put(ResultObject.PARAM_MSG, sp_out);

            //1:1获取积分
            outmap.put("amount", receipt.getTotalPrice());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("testdoTransactionByReceiptId wrong:" + e.getMessage());
            outmap.put(ResultObject.PARAM_ERRCODE, -1);
        }
        return outmap;
    }

    /**
     * 正式 交易
     *
     * @param userTag
     * @param receiptId
     * @return
     */
    @Override
    public ResultObject doTransactionByReceiptId(String userTag, String receiptId, ReceiptData receipt) {
        ResultObject result = new ResultObject();
        try {
            // 兑换积分
            Map<String, Object> map = new HashMap<>();
            map.put("userTag", userTag);
            map.put("uniqueId", receiptId);
            map.put("amount", receipt.getTotalPrice());
            map.put("type", 1);
            map.put("insertMode", "SCAN QR CODE");
            map.put("createUser", userTag);
            Map<String, Object> sp_map = this.doTransaction(map);
            Integer sp_errcode = Integer.parseInt(sp_map.get(ResultObject.PARAM_ERRCODE).toString());
            if (sp_errcode == -1) {
                result.setErrCode("-5");
                result.setMsg("用户不存在");
                return result;
            } else if (sp_errcode == -2) {
                result.setErrCode("-6");
                result.setMsg("已兑换");
                return result;
            }
            //最终积分
            String sp_out = sp_map.get("value").toString();
            result.setMsg(sp_out);

            //1:1获取积分
            result.put("amount", receipt.getTotalPrice());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("doTransactionByReceiptId wrong:" + e.getMessage());
            result.put(ResultObject.PARAM_ERRCODE, -10001);
            result.put(ResultObject.PARAM_MSG, "系统异常");
        }
        return result;
    }

    /**
     * 正式 交易
     *
     * @param userTag
     * @param receiptId
     * @return
     */
    @Override
    public ResultObject doTransactionByReceiptId(String userTag, String receiptId) {
        ResultObject r = new ResultObject();
        try {
            logger.info("积分兑换 开始, 用户:{}", userTag);
            r = ((TransactionService) AopContext.currentProxy()).getReceiptDataById(receiptId);
            Integer rt_errcode = Integer.parseInt(r.getErrCode());
            logger.info("通过小票ID查询 返回码:{}, 获取正常:{}", rt_errcode, rt_errcode != 0);
            if (rt_errcode != 0) {
                return r;
            }
            ReceiptData receipt = (ReceiptData) r.getData();
            logger.info("通过小票ID查询小票数据:" + receipt.toString());

            r = this.doTransactionByReceiptId(userTag, receiptId, receipt);
            r.setData(receipt);


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("doTransactionByReceiptId wrong:" + e.getMessage());
            r.put(ResultObject.PARAM_ERRCODE, -10001);
        } finally {
            logger.info("积分兑换 结束, 用户:{}", userTag);
        }
        return r;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultObject getReceiptDataById(String receiptId) {
        ResultObject result = new ResultObject();
        //根据state(小票唯一标识) -> 接口 - > 获取票信息
        ReceiptData receipt = null;
        try {
            ReceiptDataHandler receiptDataHandler = new ReceiptDataHandler(receiptId, result).invoke();
            if (receiptDataHandler.is()) return result;
            receipt = receiptDataHandler.getReceipt();

            result.setData(receipt);
        } catch (IOException e) {
            e.printStackTrace();
            result.setErrCode("-1");
            logger.info("获取小票数据异常, 小票ID:" + receiptId);
        }
        return result;
    }

    /**
     * @param map
     * @return 总积分 -1失败
     */
    private Map<String, Object> doTransaction(Map<String, Object> map) {
        //Integer out = 0;
        Map<String, Object> outmap = new HashMap<>();
        try {
            String userTag = map.get("userTag").toString();
            String uniqueId = map.get("uniqueId").toString();
            Double amount = Double.valueOf(map.get("amount").toString());
            Integer type = Integer.valueOf(map.get("type").toString());
            String insertMode = map.get("insertMode").toString();
            String createUser = map.get("createUser").toString();

            logger.info("SP_TRANSACTION('" + userTag + "', '" + uniqueId + "', " + amount + ", " + type + ", '" + insertMode + "', '" + createUser + "', @out)");
            SqlSession session = super.getSqlSession();
            String statesql = "com.cjcx.member.dao.TransactionDao.storageScore";
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("userTag", userTag);
            searchMap.put("uniqueId", uniqueId);
            searchMap.put("amount", amount);
            searchMap.put("type", type);
            searchMap.put("insertMode", insertMode);
            searchMap.put("createUser", createUser);
            session.selectOne(statesql, searchMap);

            String result = searchMap.get("state_out").toString();
            logger.info("SP_TRANSACTION Storage Score out:" + result);
            if ("-1".equals(result) || "-2".equals(result)) {
                outmap.put(ResultObject.PARAM_ERRCODE, result);
            } else {
                outmap.put(ResultObject.PARAM_ERRCODE, 0);
                outmap.put("value", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, "-1");
            logger.info("doTransaction wrong:" + e.getMessage());
        }
        return outmap;
    }


    private class ReceiptDataHandler {
        private boolean myResult;
        private String receiptId;
        private ResultObject r;
        private ReceiptData receipt;

        public ReceiptDataHandler(String receiptId, ResultObject r) {
            this.receiptId = receiptId;
            this.r = r;
        }

        boolean is() {
            return myResult;
        }

        public ReceiptData getReceipt() {
            return receipt;
        }

        public ReceiptDataHandler invoke() throws java.io.IOException {
            String content;//接口
            Map<String, SystemParamDto> paramMap = cacheHolder.getSystemParamMap();
            SystemParamDto systemParamDto = paramMap.get(SystemParam.KEY_RECEIPT_FOR_QR_CODE);
            String receiptForQrcodeUrl = systemParamDto.getParamValue();
            logger.info("小票数据 - 请求接口地址:" + receiptForQrcodeUrl);
            try {
                //接口 -> 小票数据
                content = RequestUtil.doPostByJson(receiptForQrcodeUrl, "{'receiptId' : '" + receiptId + "' }");
            } catch (Exception e) {
                e.printStackTrace();
                r.setErrCode("-3");
                r.setMsg("获取接口数据错误");
                myResult = true;
                return this;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Map receiptMap = mapper.readValue(content, Map.class);

            Integer returnCode = Integer.parseInt(receiptMap.get("returnCode").toString());
            logger.info("小票数据 - 返回状态码:" + returnCode);
            if (returnCode != 0) {
                r.setErrCode(returnCode.toString());
                r.setMsg(receiptMap.get("message").toString());
                myResult = true;
                return this;
            }

            //获取票信息
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ReceiptData.class);
            String receiptJson = GsonUtil.toJson(receiptMap.get("data"));
            List<ReceiptData> list = mapper.readValue(receiptJson, javaType);

            receipt = list.get(0);
            logger.debug("小票数据 - 内容:" + receipt.toString());
            //重打单
            if (receipt.getIsRepeat() == 1) {
                r.setErrCode("-4");
                r.setMsg("无效小票 - 1");
                myResult = true;
                return this;
            }
            //无效单
            if (receipt.getReceiptStatus() == 1) {
                r.setErrCode("-4");
                r.setMsg("无效小票 - 2");
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }

    /**
     * 查询交易结果
     *
     * @param openId
     * @param receiptId
     * @return errorCode  0 兑换成功
     * -1 用户不存在
     * -2
     * -3 未检测到交易信息
     * -4 未兑换
     */
    @Override
    public Map<String, Object> searchTransaction(String openId, String receiptId) {
        Map<String, Object> map = new HashMap<>();
        try {
            //检查用户信息
            UserDto userDto = new UserDto();
            userDto.setOpenId(openId);
            List<UserDto> list = userService.findByCondition(userDto);
            if (list.size() == 0) {
                map.put(ResultObject.PARAM_ERRCODE, -1);
                map.put(ResultObject.PARAM_MSG, "用户不存在");
                return map;
            }
            //检查小票信息


            //检查交易信息
            TransactionDto transactionDto = this.getTransactionRecord(openId, receiptId);
            if (transactionDto == null) {
                map.put(ResultObject.PARAM_ERRCODE, -3);
                map.put(ResultObject.PARAM_MSG, "未检测到交易信息");
            } else if (transactionDto.getState() == 1) {
                map.put(ResultObject.PARAM_ERRCODE, 0);
                map.put(ResultObject.PARAM_MSG, "兑换成功");
            } else if (transactionDto.getState() == 0) {
                map.put(ResultObject.PARAM_ERRCODE, -4);
                map.put(ResultObject.PARAM_MSG, "未兑换");
            }
        } finally {

        }
        return map;
    }

    public TransactionDto getTransactionRecord(String openId, String receiptId) {
        return transactionDao.searchTransaction(openId, receiptId);
    }
}
