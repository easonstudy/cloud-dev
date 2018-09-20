package com.cjcx.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ServletComponentScan
//告诉Spring 哪个packages的用注解标识的类 会被spring自动扫描并且装入bean容器。
@ComponentScan(basePackages = {"com.cjcx.mall"})
//Aop代理
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages={"com.cjcx.amll.dao"})
@SpringBootApplication
public class MallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallApplication.class, args);
	}
}
