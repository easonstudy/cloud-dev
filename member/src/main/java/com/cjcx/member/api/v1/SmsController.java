package com.cjcx.member.api.v1;

import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserService;
import com.cjcx.member.utils.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/{phone}")
    public ResultObject send(@PathVariable("phone") String phone,
                             @RequestParam("type") Integer type) {
        ResultObject result = new ResultObject();
        result = userService.sendSmsCode(phone, type);
        logger.info("短信 返回消息:{}", GsonUtil.toJson(result));
        return result;
    }

}
