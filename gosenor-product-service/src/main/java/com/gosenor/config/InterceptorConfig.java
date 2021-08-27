package com.gosenor.config;

import com.gosenor.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-28 10:31
 */
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/api/cart/*/**","/api/product/*/lock_products")
                .excludePathPatterns("");

        //WebMvcConfigurer.super.addInterceptors(registry);
    }
}
