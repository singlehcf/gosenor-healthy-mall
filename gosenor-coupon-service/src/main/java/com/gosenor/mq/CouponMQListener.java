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

        log.info("监听到消息：releaseCouponRecord消息内容：{}", recordMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();

        boolean flag = couponRecordService.releaseCouponRecord(recordMessage);

        //防止同个解锁任务并发进入；如果是串行消费不用加锁；加锁有利也有弊，看项目业务逻辑而定
        //Lock lock = redissonClient.getLock("lock:coupon_record_release:"+recordMessage.getTaskId());
        //lock.lock();
        try {
            if (flag) {
                //确认消息消费成功
                channel.basicAck(msgTag, false);
            }else {
                log.error("释放优惠券失败 flag=false,{}",recordMessage);
                channel.basicReject(msgTag,true);
            }

        } catch (IOException e) {
            log.error("释放优惠券记录异常:{},msg:{}",e,recordMessage);
            channel.basicReject(msgTag,true);
        }
    }


}
