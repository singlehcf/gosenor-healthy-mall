package com.gosenor.vo;

import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description: 轮播图
 * @author: hcf
 * @create: 2021-08-11 11:53
 */
@Data
public class BannerVO {
    private Integer id;

    /**
     * 图 ⽚
     */
    private String img;

    /**
     * 跳转	地址
     */
    private String url;

    /**
     * 权᯿
     */
    private Integer weight;
}
