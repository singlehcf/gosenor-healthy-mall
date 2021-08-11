package com.gosenor.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description: 用户登陆
 * @author: hcf
 * @create: 2021-07-27 09:15
 */
@ApiModel("用户登陆")
@Data
public class UserLoginRequest {

    @ApiModelProperty(value = "账号",required = true)
    private String account;

    @ApiModelProperty(value = "密码",example = "12345")
    private String pwd;
}
