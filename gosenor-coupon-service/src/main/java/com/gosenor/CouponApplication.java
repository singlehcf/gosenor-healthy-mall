package com.gosenor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-02 10:06
 */
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.gosenor.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class CouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class);
    }
}
