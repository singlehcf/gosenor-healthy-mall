package com.gosenor.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author hcf
 * @since 2021-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product_order")
public class ProductOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单唯⼀标识
     */
    private String outTradeNo;

    /**
     * NEW	未⽀付订单,PAY已经⽀付订单,CANCEL超时取消订单
     */
    private String state;

    /**
     * 订单⽣成时间
     */
    private Date createTime;

    /**
     * 订单总⾦额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实际⽀付价格
     */
    private BigDecimal payAmount;

    /**
     * ⽀付类型，微信-银⾏-⽀付宝
     */
    private String payType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * ⽤户	id
     */
    private Integer userId;

    /**
     * 0表示未删除，	1表示已经删除
     */
    private Integer del;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单类型 DAILY普通单，PROMOTION促销订单
     */
    private String orderType;

    /**
     * 收货地址 json存储
     */
    private String receiverAddress;


}
