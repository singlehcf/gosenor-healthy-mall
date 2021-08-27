package com.gosenor.model;

import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-27 11:18
 */
@Data
public class ProductMessage {
    /**
     * 消息队列id
     */
    private Long messageId;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 库存锁定⼯作单id
     */
    private Long taskId;
}
