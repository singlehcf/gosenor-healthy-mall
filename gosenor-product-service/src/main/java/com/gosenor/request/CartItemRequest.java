package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-12 14:34
 */
@ApiModel("购物车item")
@Data
public class CartItemRequest {
    @ApiModelProperty(value = "商品id",example = "11")
    @JsonProperty("product_id")
    private long productId;

    @ApiModelProperty(value = "购买数量",example = "1")
    @JsonProperty("buy_num")
    private int buyNum;
}
