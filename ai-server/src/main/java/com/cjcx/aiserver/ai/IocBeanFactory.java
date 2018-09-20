package com.cjcx.aiserver.ai;

import com.cjcx.aiserver.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class IocBeanFactory {

    private static HashMap<String, String> beanHandlerMap = new HashMap<>();
    private static HashMap<String, String> beanManagerMap = new HashMap<>();

    static {
        beanHandlerMap.put("B_image", "baiduImageHandler");
        beanHandlerMap.put("A_image", "aliyunImageHandler");
        beanHandlerMap.put("T_image", "tencentImageHandler");
        beanHandlerMap.put("B_face", "baiduFaceHandler");
        beanHandlerMap.put("A_face", "aliyunFaceHandler");
        beanHandlerMap.put("T_face", "tencentFaceHandler");

        beanManagerMap.put("B", "baiduAccountManager");
        beanManagerMap.put("A", "aliyunAccountManager");
        beanManagerMap.put("T", "tencentAccountManager");
    }

    /**
     * 从IOC容器取 ExcuteInterface的实现类
     *
     * @param beanKey
     * @return
     */
    public static ExcuteInterface getHandler(String beanKey) {
        return SpringUtil.getBean(beanHandlerMap.get(beanKey));
    }

    public static AbstractAccountManager getAccountManager(String beanKey) {
        return SpringUtil.getBean(beanManagerMap.get(beanKey));
    }

    //创建Java对象
    //1:new 类名
    //2:Class.forName().newInstance();有无参构造函数
    //3:getConstructor();
    // Constructor<User> constructor = User.class.getConstructor();
    // User user = constructor.newInstance();
    //4:Clone
    //  user.clone();
    //target = (AbstractImageHandler)Class.forName(this.targetBeanName).newInstance();
    //log.info("target: {}, bean:{}" , target, targetBeanName);
}
