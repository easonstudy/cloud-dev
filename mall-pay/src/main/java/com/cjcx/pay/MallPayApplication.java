package com.cjcx.pay;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Properties;

//扫描servlet 文件
@ServletComponentScan
//告诉Spring 哪个packages的用注解标识的类 会被spring自动扫描并且装入bean容器。
//@ComponentScan(basePackages = {"com.cjcx.pay"})
//Mybaits dao扫描
@MapperScan("com.cjcx.pay.dao")
//Aop代理
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy = true)
//定时任务
@EnableScheduling

@EnableAsync
@SpringBootApplication
public class MallPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallPayApplication.class, args);
	}

	//配置mybatis的分页插件pageHelper
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql");    //配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}

	//配置 @EnbleScheduling 线程池大小, 默认是单线程
	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		// 线程池大小
		scheduler.setPoolSize(2);
		// 线程名称
		scheduler.setThreadNamePrefix("scheduler-task");
		return scheduler;
	}


}
