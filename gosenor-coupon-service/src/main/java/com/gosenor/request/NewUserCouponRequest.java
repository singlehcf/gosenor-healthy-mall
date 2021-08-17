package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-17 13:46
 */
@Data
public class NewUserCouponRequest {


    @ApiModelProperty(value = "用户Id",example = "19")
    @JsonProperty("user_id")
    private long userId;


    @ApiModelProperty(value = "名称",example = "小姐姐")
    @JsonProperty("name")
    private String name;

}
