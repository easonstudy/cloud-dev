package com.cjcx.pay.startup;

import com.cjcx.pay.cache.CacheHolder;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "initServlet", loadOnStartup=0, urlPatterns={"/initServlet"})
public final class InitServlet extends HttpServlet {
	
	 private static Logger logger = Logger.getLogger(InitServlet.class);
	 
	 private static final long serialVersionUID = 1L;
	 
	 private CacheHolder cacheHolder;
	 
	 public void init(ServletConfig config) throws ServletException {
		logger.info("InitServlet  start");
		super.init(config);
		ServletContext sc = config.getServletContext();
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(sc);
		cacheHolder = (CacheHolder)wc.getBean("cacheHolder");
		sc.setAttribute("cacheHolder",cacheHolder);
		cacheHolder.start(1000L*2);
		logger.info("InitServlet end");
	 }
	 
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 }
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 }
	 
}
