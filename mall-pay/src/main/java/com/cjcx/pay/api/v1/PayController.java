package com.cjcx.pay.api.v1;

import com.cjcx.pay.dto.filter.PayDto;
import com.cjcx.pay.framework.controller.AdminSupport;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.user.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pay")
public class PayController extends AdminSupport {

    @Autowired
    ITransactionService service;


    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public ResultObject receiveMallMessage(@RequestBody PayDto dto) {
        ResultObject result = new ResultObject();

        result = service.storedTransaction(dto);

        return result;
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public ResultObject receive(PayDto dto) {
        ResultObject result = new ResultObject();

        result = service.storedTransaction(dto);

        return result;
    }
}
