package com.cjcx.wechat.open.config;


import com.cjcx.wechat.open.interceptor.CorsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by sun on 2017-3-21.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 此方法把该拦截器实例化成一个bean,否则在拦截器里无法注入其它bean
     *
     * @return
     */

    @Bean
    CorsInterceptor corsInterceptor() {
        return new CorsInterceptor();
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);

        //  /跳转至index
        registry.addViewController("/").setViewName("index");

    }

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //跨域
        registry.addInterceptor(corsInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }

}
