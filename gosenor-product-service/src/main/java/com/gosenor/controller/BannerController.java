package com.gosenor.controller;

import com.gosenor.service.BannerService;
import com.gosenor.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: gosenor-healthy-mall
 * @description: 轮播图模块
 * @author: hcf
 * @create: 2021-08-11 11:50
 */
@Api(tags = "轮播图模块")
@RestController
@RequestMapping("/api/banner/v1")
@Slf4j
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @ApiOperation("轮播图列表接口")
    @GetMapping("list")
    public JsonData list(){

        return JsonData.buildSuccess(bannerService.list());

    }
}
