package com.cjcx.aiserver;

import com.cjcx.aiserver.ai.baidu.BaiduAccountManager;
import com.cjcx.aiserver.utils.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestIOC {


    @Test
    public void test(){



        BaiduAccountManager accountManager = SpringUtil.getBean("baiduAccountManager");



        /*BaiduAccountManager accountManager2 = SpringUtil.getBean("baiduAccountManager");

        //System.out.println("a:"+BaiduAccountManager.a);

        System.out.println(accountManager2 == accountManager);*/

    }

}
