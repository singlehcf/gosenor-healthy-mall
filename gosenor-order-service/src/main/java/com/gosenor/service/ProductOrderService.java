package com.gosenor.service;

import com.gosenor.request.ConfirmOrderRequest;
import com.gosenor.utils.JsonData;

public interface ProductOrderService {
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);
}
