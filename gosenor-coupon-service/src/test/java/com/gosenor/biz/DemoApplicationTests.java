package com.gosenor.biz;

import com.gosenor.CouponApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-25 14:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CouponApplication.class)
@Slf4j
public class DemoApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send(){

        rabbitTemplate.convertAndSend("coupon.event.exchange","coupon.release.delay.routing.key","5qeqweqw");

    }
}
