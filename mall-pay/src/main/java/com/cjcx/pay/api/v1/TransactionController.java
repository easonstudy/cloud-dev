package com.cjcx.pay.api.v1;

import com.cjcx.pay.dto.TransactionDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.framework.annotation.AuthToken;
import com.cjcx.pay.framework.controller.BaseController;
import com.cjcx.pay.framework.service.IBaseService;
import com.cjcx.pay.framework.web.PageParams;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.user.ITransactionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController extends BaseController<TransactionDto, Integer> {

    @Autowired
    ITransactionService transactionService;

    @Override
    protected IBaseService<TransactionDto, Integer> getDefaultService() {
        return transactionService;
    }

    @AuthToken
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultObject list(FilterDto filterDto) {
        ResultObject result = new ResultObject();
        try {
            PageParams params = super.getPageParams(request);
            if (params.isEnabled()) {
                PageHelper.startPage(params.getPageNum(), params.getPageSize());
            }
            PageHelper.orderBy("create_time desc");
            List<TransactionDto> list = transactionService.getTransactionList(filterDto);

            Page<TransactionDto> page = (Page<TransactionDto>) list;
            PageInfo<TransactionDto> pageInfo = new PageInfo<TransactionDto>(page);
            result.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
