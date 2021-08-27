package com.gosenor.service;

import com.gosenor.request.ConfirmOrderRequest;
import com.gosenor.utils.JsonData;

public interface ProductOrderService {
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);

    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);
}
