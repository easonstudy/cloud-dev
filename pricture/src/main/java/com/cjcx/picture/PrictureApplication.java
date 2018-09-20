package com.cjcx.picture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//告诉Spring 哪个packages的用注解标识的类 会被spring自动扫描并且装入bean容器。
@Configuration
@ComponentScan(basePackages = {"com.cjcx.picture"})
@SpringBootApplication
public class PrictureApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrictureApplication.class, args);
    }

    /*@Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);    //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(50 * 1024 * 1024);    //上传文件大小 50M 50*1024*1024
        return resolver;
    }*/
}


