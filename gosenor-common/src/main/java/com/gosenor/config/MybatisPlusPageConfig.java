package com.gosenor.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: gosenor-healthy-mall
 * @description: mybatis-plus配置文件
 * @author: hcf
 * @create: 2021-08-02 10:11
 */
@Configuration
public class MybatisPlusPageConfig {
    /*public PaginationInterceptor paginationInnerInterceptor(){
        return new PaginationInterceptor();
    }*/

    /**
     * 新的分⻚插件,⼀缓和⼆缓遵循mybatis的规则,
     * 需要设置
     MybatisConfiguration#useDeprecatedExecutor =
     false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

    /*@Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }*/
}
