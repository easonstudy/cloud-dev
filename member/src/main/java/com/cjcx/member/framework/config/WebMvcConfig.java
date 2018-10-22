package com.cjcx.member.framework.config;


import com.cjcx.member.framework.interceptor.AuthTokenInterceptor;
import com.cjcx.member.framework.interceptor.CorsInterceptor;
import com.cjcx.member.framework.interceptor.PermissionInterceptor;
import com.cjcx.member.framework.interceptor.SessionInterceptor;
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
     * @return
     */
    @Bean
    SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    CorsInterceptor corsInterceptor(){ return new CorsInterceptor(); }

    @Bean
    PermissionInterceptor permissionInterceptor(){ return new PermissionInterceptor(); }

    @Bean
    AuthTokenInterceptor authTokenInterceptor(){ return new AuthTokenInterceptor(); }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //super.addViewControllers(registry);
        registry.addViewController("/").setViewName("index");
    }

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //跨域
        registry.addInterceptor(corsInterceptor()).addPathPatterns("/**");

        //权限
        registry.addInterceptor(permissionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/error","/vcode/gif","/index");

        //AuthToken
        registry.addInterceptor(authTokenInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }

}
