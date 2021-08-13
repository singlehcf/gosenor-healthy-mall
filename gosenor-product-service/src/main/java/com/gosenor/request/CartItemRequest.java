package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-12 14:34
 */
@ApiModel("购物车item")
@NoArgsConstructor
@Data
public class CartItemRequest {
    @NotNull
    @ApiModelProperty(value = "商品id",example = "11")
    @JsonProperty("product_id")
    private long productId;

    @Range(min=1, max=100,message = "购买数量1-100")
    @NotNull
    @ApiModelProperty(value = "购买数量",example = "1")
    @JsonProperty("buy_num")
    private int buyNum;
}
