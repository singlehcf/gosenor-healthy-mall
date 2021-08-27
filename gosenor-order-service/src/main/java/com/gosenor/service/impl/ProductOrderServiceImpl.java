package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gosenor.mapper.ProductOrderMapper;
import com.gosenor.model.ProductOrderDO;
import com.gosenor.request.ConfirmOrderRequest;
import com.gosenor.service.ProductOrderService;
import com.gosenor.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * * 防重提交
 * * 用户微服务-确认收货地址
 * * 商品微服务-获取最新购物项和价格
 * * 订单验价
 * * 优惠券微服务-获取优惠券
 * * 验证价格
 * * 锁定优惠券
 * * 锁定商品库存
 * * 创建订单对象
 * * 创建子订单对象
 * * 发送延迟消息-用于自动关单
 * * 创建支付信息-对接三方支付
 * @author: hcf
 * @create: 2021-08-13 14:20
 */
@Service
@Slf4j
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Override
    public JsonData confirmOrder(ConfirmOrderRequest orderRequest) {
        return null;
    }

    @Override
    public String queryProductOrderState(String outTradeNo) {
        ProductOrderDO productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>().eq("out_trade_no",outTradeNo));
        if (productOrderDO == null){
            return "";
        }
        return productOrderDO.getState();
    }
}
