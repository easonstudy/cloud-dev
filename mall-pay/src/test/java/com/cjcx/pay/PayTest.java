package com.cjcx.pay;


import com.cjcx.pay.service.user.IPayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PayTest {

    @Autowired
    IPayService payService;

    @Test
    public void test(){

        payService.submitToThirdPay(null);

    }


}
