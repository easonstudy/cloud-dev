package com.cjcx.member.api.v1;

import com.cjcx.member.dto.TransactionDto;
import com.cjcx.member.dto.UserDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.BaseController;
import com.cjcx.member.framework.service.IBaseService;
import com.cjcx.member.framework.web.PageParams;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.ITransactionService;
import com.cjcx.member.service.user.IUserService;
import com.cjcx.member.utils.GsonUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.templatemessage.TemplateMessage;
import weixin.popular.bean.message.templatemessage.TemplateMessageItem;

import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController extends BaseController<TransactionDto, Integer> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService userService;

    @Autowired
    ITransactionService transactionService;

    @Override
    protected IBaseService<TransactionDto, Integer> getDefaultService() {
        return transactionService;
    }

    /**
     * 测试 积分兑换
     *
     * @param openId
     * @param receiptId
     * @return
     */
    @RequestMapping(value = "/test/score", method = RequestMethod.GET)
    public Map<String, Object> score(@RequestParam("openId") String openId, @RequestParam("receiptId") String receiptId) {
        Map map = new HashMap();
        try {
            logger.info("test api openId:" + openId + "receiptId:" + receiptId);
            double price = Double.parseDouble(receiptId.split("_")[1]);
            //测试-创建交易信息
            map = transactionService.testdoTransactionByReceiptId(openId, receiptId, price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 正式 - 积分兑换
     *
     * @param openId
     * @param receiptId
     * @return
     */
    @RequestMapping(value = "/score")
    public ResultObject exchange(@RequestParam(value = "openId", required = false) String openId,
                                 @RequestParam(value = "phone", required = false) String phone,
                                 @RequestParam("receiptId") String receiptId) {
        ResultObject r = new ResultObject();
        try {
            logger.info("/transaction/score 接收参数: openId:" + openId + ", phone:" + phone + ", receiptId:" + receiptId);
            if(openId == null && phone != null) {
                openId = phone;
            }
            String userTag = openId;
            r = transactionService.doTransactionByReceiptId(userTag, receiptId);
            logger.info("/transaction/score 返回消息:{}" , GsonUtil.toJson(r));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 积分兑换结果-查询
     *
     * @param openId
     * @param receiptId
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("openId") String openId,
                         @RequestParam("receiptId") String receiptId) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = transactionService.searchTransaction(openId, receiptId);
            logger.debug("filter transaction:" + GsonUtil.toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GsonUtil.toJson(map);
    }


    @AuthToken
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultObject list() {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            PageHelper.orderBy("create_time desc");
            TransactionDto condition = new TransactionDto();
            List<TransactionDto> list = transactionService.findByCondition(condition);

            Page<TransactionDto> page = (Page<TransactionDto>) list;
            PageInfo<TransactionDto> pageInfo = new PageInfo<TransactionDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
