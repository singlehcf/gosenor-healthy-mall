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
 * @create: 2021-08-02 14:30
 */
@Data
public class CouponVO {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 优惠卷类型[NEW_USER注册赠券，TASK任务卷，PROMOTION	促销劵]
     */
    private String category;

    /**
     * 发布状态, PUBLISH发布，DRAFT草稿，OFFLINE下线
     */
    private String publish;

    /**
     * 优惠券图⽚
     */
    @JsonProperty("coupon_img")
    private String couponImg;

    /**
     * 优惠券标题
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * 抵扣价格
     */
    private BigDecimal price;

    /**
     * 每	⼈限制张数
     */
    @JsonProperty("user_limit")
    private Integer userLimit;

    /**
     * 优惠券开始有效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale = "zh",timezone = "GMT+8")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * 优惠	券失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",locale = "zh",timezone = "GMT+8")
    @JsonProperty("end_time")
    private Date endTime;

    /**
     * 优惠券总ᰁ
     */
    @JsonProperty("publish_count")
    private Integer publishCount;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 满多少才可以使⽤
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;
}
