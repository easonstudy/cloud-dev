package com.cjcx.pay.controller;

import com.cjcx.pay.service.user.IAopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopController {


    @Autowired
    IAopService aopService;


    /**
     * 首页
     * @return
     */
    @GetMapping("/aop")
    @ResponseBody
    String aop() {
        //逻辑处理
        aopService.demo("guo", 13);
        return "index";
    }
}
