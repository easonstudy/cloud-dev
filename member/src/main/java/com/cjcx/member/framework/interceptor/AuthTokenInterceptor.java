package com.cjcx.member.framework.interceptor;

import com.cjcx.member.dto.StaffDto;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.service.ILoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthTokenInterceptor extends HandlerInterceptorAdapter {

    protected static final Logger logger = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    @Autowired
    ILoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //logger.info("preHandle" + handler.getClass());
        /*Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            logger.info(name+ ":" +value);
        }*/
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            logger.info("can not cast handler to HandlerMethod.class");
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        //logger.info("Method name:" + method.getName());
        AuthToken noAuthToken = method.getAnnotation(AuthToken.class);
        // 声明就需要验证TOKEN
        if (noAuthToken != null) {
            String token = request.getHeader("Authorization");
            if (!StringUtils.isEmpty(token) && token.length() == 32) {

                //DB中取
                StaffDto staff = loginService.getUserByToken(token);
                boolean isValid = staff == null ? false : true;

                //Redis中取
                //String key = "login:token:" + token;
                //boolean isValid = JedisUtils.exists(key);
                //logger.debug("AuthToken- redis.key=>" + "login:token:" + ", 存在:" + isValid);
                //JedisUtils.setex(key, token, 1 * 60 * 30);

                //boolean isValid = true;
                if (isValid) {
                    //logger.debug("Authorization:" + token + " return true");
                    return true;
                } else {
                    //logger.debug("Authorization:" + token + "  return 401");
                    //response.setStatus(401);
                    response.getWriter().print("{\"errorCode\": 40002}");
                    return false;
                }
            } else {
                logger.info("Authorization:" + token + " return 40001");
                response.getWriter().print("{\"errorCode\": 40001}");
                return false;
            }
        }
        return true;
    }
}
