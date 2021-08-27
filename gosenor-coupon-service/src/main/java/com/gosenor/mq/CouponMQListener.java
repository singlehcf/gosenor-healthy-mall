package com.gosenor.mq;

import com.gosenor.mapper.CouponRecordMapper;
import com.gosenor.model.CouponRecordMessage;
import com.gosenor.model.CouponTaskDO;
import com.gosenor.service.CouponRecordService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-25 15:22
 */
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.coupon_release_queue}")
public class CouponMQListener {
    @Autowired
    private CouponRecordService couponRecordService;

    @RabbitHandler
    public void releaseCouponRecord(CouponRecordMessage recordMessage, Message message, Channel channel) throws IOException {

        long tag = message.getMessageProperties().getDeliveryTag();
        boolean flag = couponRecordService.releaseCouponRecord(recordMessage);
        if (flag){
            channel.basicAck(tag,false);
        }else {
            channel.basicReject(tag,true);
        }
        //获取任务
       /* if (couponTaskDO == null){
            channel.basicAck(tag,false);
        }*/
        //获取优惠卷详情
        //CouponRecordDO couponRecordDO = couponRecordMapper.selectById(couponTaskDO.getCouponRecordId());

        //获取订单信息
    }


}
