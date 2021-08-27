package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-27 10:38
 */
@Data
public class OrderItemRequest {

    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("buy_num")
    private int buyNum;
}
