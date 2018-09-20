package com.cjcx.member.framework.service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public abstract class AdminService extends SqlSessionDaoSupport {

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
        super.setSqlSessionFactory(sqlSessionFactory);

    }

    public HttpServletRequest getHttpServletRequeset(){
        HttpServletRequest request = null;
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

}
