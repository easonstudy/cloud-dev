package com.cjcx.pay.service.user.impl;

import com.cjcx.pay.service.user.IAopService;
import org.springframework.stereotype.Service;

@Service
public class AopService implements IAopService {

    @Override
    public String demo(String name, Integer age) {
        return "hello " + name +", age is " + age;
    }
}
