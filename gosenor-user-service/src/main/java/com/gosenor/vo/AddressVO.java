package com.gosenor.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: gosenor-healthy-mall
 * @description: 地址
 * @author: hcf
 * @create: 2021-07-29 13:39
 */
@ApiModel("用户地址")
@Data
public class AddressVO {
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否默认收货地址：0->否；1->是
     */
    @JsonProperty("default_status")
    @ApiModelProperty("是否默认收货地址：0->否；1->是")
    private Integer defaultStatus;

    /**
     * 收发货人姓名
     */
    @JsonProperty("receive_name")
    @ApiModelProperty("收发货人姓名")
    private String receiveName;

    /**
     * 收货人电话
     */
    @ApiModelProperty("收货人电话")
    private String phone;

    /**
     * 省/直辖市
     */
    @ApiModelProperty("省/直辖市")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String region;

    /**
     * 详细地址
     */
    @JsonProperty("detail_address")
    @ApiModelProperty("详细地址")
    private String detailAddress;

}
