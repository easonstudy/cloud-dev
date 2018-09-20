package com.cjcx.pay.framework.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.log4j.Logger;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by gmh on 2017/6/7 0007.
 */

@Configuration
@EnableTransactionManagement
public class DataBaseConfiguration implements EnvironmentAware {
    private Logger logger = Logger.getLogger(DataBaseConfiguration.class);
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "druid.");
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public DataSource dataSource() {
        //System.out.println("注入druid！！！");
        logger.info("注入druid！！！");

        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        datasource.setUrl(propertyResolver.getProperty("url"));
        datasource.setUsername(propertyResolver.getProperty("username"));
        datasource.setPassword(propertyResolver.getProperty("password"));
        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        datasource.setTimeBetweenEvictionRunsMillis(Long.valueOf(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        return datasource;
    }
}
