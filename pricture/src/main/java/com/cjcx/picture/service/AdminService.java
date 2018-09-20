package com.cjcx.picture.service;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public abstract class AdminService {

    public HttpServletRequest getHttpServletRequeset() {
        HttpServletRequest request = null;
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
