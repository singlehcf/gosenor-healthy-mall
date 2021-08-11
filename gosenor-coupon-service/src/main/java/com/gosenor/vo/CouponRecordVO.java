package com.gosenor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-11 10:55
 */
@Data
public class CouponRecordVO {
    private Long id;

    /**
     * 优惠券id
     */
    @JsonProperty("coupon_id")
    private Long couponId;

    /**
     * 创建时间获得时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale = "zh",timezone = "GMT+8")
    @JsonProperty("create_time")
    private Date createTime;

    /**
     * 使⽤状态 可⽤ NEW,已使⽤USED,过期 EXPIRED;
     */
    @JsonProperty("use_state")
    private String useState;

    /**
     * ⽤ 户id
     */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * ⽤户昵称
     */
    @JsonProperty("user_name")
    private String userName;

    /**
     * 优惠券标题
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale = "zh",timezone = "GMT+8")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * 结束	时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale = "zh",timezone = "GMT+8")
    @JsonProperty("end_time")
    private Date endTime;

    /**
     * 订单id
     */
    @JsonProperty("order_id")
    private Long orderId;

    /**
     * 抵扣价格
     */
    private BigDecimal price;

    /**
     * 满多少才可以使⽤
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;
}
