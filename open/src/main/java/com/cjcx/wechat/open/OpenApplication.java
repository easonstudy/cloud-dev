package com.cjcx.wechat.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 微信公众号
 */
//扫描servlet 文件
@ServletComponentScan
//告诉Spring 哪个packages的用注解标识的类 会被spring自动扫描并且装入bean容器。
@Configuration
@ComponentScan(basePackages = {"com.cjcx.wechat.open"})
@SpringBootApplication
public class OpenApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenApplication.class, args);
	}
}
