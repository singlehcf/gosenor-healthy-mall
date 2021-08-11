package com.gosenor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: gosenor-healthy-mall
 * @description: 地址新增
 * @author: hcf
 * @create: 2021-07-29 10:09
 */
@Data
public class AddressAddRequest {

    /**
     * 是否默认收货地址：0->否；1->是
     */
    @ApiModelProperty(value = "是否是否默认收货地址，0->否；1->是",example = "0",required = true)
    @JsonProperty("default_status")
    private Integer defaultStatus;

    /**
     * 收发货人姓名
     */
    @ApiModelProperty(value = "收发货人姓名",example = "隔壁老王",required = true)
    @JsonProperty("receive_name")
    private String receiveName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话",example = "13300001111",required = true)
    private String phone;

    /**
     * 省/直辖市
     */
    @ApiModelProperty(value = "省/直辖市",example = "四川",required = true)
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市",example = "成都",required = true)
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区",example = "高新区",required = true)
    private String region;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",example = "运营中心-老王隔壁1号",required = true)
    @JsonProperty("detail_address")
    private String detailAddress;


}
