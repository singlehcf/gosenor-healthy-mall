package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-27 10:32
 */
@Data
public class LockProductRequest {

    @JsonProperty("order_out_trade_no")
    private String orderOutTradeNo;

    @JsonProperty("order_item_list")
    private List<OrderItemRequest> orderItemList;
}
