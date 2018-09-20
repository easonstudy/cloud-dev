package com.cjcx.picture.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsInterceptor extends HandlerInterceptorAdapter {

	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	 @Override  
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	     //logger.info("CorsInterceptor preHandle");
	 	 //表明它允许"*"发起跨域请求
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 //表明它允许跨域请求包含content-type头
		 response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, Authorization-Token");
		 //表明在3628800秒内，不需要再发送预检验请求，可以缓存该结果
		 response.setHeader("Access-Control-Max-Age", "3000");
		 //表明它允许GET、PUT、DELETE、POST的外域请求
		 response.setHeader("Access-Control-Allow-Methods", "GET,PUT,DELETE,POST,OPTIONS,PATCH");
		 //客户端可以带上cookie
		 //response.setHeader("Access-Control-Allow-Credentials", "true");
		 return true;
	 }
}
