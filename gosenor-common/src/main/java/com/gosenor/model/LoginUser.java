package com.gosenor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-27 14:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    @JsonProperty("head_img")
    private String headImg;

    /**
     * 邮箱
     */
    private String mail;
}
