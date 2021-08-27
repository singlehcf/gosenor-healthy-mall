package com.gosenor.biz;

import com.gosenor.CouponApplication;
import com.gosenor.feign.OrderFeignService;
import com.gosenor.model.CouponRecordMessage;
import com.gosenor.utils.JsonData;
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

    @Autowired
    private OrderFeignService orderFeignService;
    @Test
    public void send(){
      /*  CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
        couponRecordMessage.setTaskId(1999L);
        couponRecordMessage.setOutTradeNo(null);
        rabbitTemplate.convertAndSend("coupon.event.exchange","coupon.release.delay.routing.key",couponRecordMessage);
        log.info("优惠券锁定消息发送成功: {}",couponRecordMessage.toString());*/
        //rabbitTemplate.convertAndSend("coupon.event.exchange","coupon.release.delay.routing.key","5qeqweqw");

    }
}
