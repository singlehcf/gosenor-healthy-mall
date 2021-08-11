package com.gosenor.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: gosenor-healthy-mall
 * @description: 用户信息
 * @author: hcf
 * @create: 2021-07-29 11:22
 */
@ApiModel(description = "用户信息")
@Data
public class UserVO {
    private Long id;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String name;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    @JsonProperty("head_img")
    private String headImg;

    /**
     * 用户签名
     */
    @ApiModelProperty("用户签名")
    private String slogan;

    /**
     * 0表示女，1表示男
     */
    private Integer sex;

    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer points;

    /**
     * 邮箱
     */
    private String mail;

    @ApiModelProperty("性别")
    private String sexName;


    public void setSex(Integer sex) {
        this.sex = sex;
        if (sex != null){
            if (sex == 1){
                //男
                this.setSexName("男");
            }else if (sex == 0){
                //女
                this.setSexName("女");
            }
        }
    }
}
