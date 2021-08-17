package com.gosenor.controller;

import com.gosenor.enums.CouponEnum;
import com.gosenor.request.NewUserCouponRequest;
import com.gosenor.service.CouponService;
import com.gosenor.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: gosenor-healthy-mall
 * @description: 优惠券模块
 * @author: hcf
 * @create: 2021-08-02 13:48
 */
@Api(tags = "优惠券模块")
@RequestMapping("/api/coupon/v1")
@RestController
@Slf4j
public class CouponController {
    @Autowired
    private CouponService couponService;

    @ApiOperation("分页查询优惠券")
    @GetMapping("/page_coupon")
    public JsonData pageCouponList(
            @ApiParam(value = "当前页")  @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "每页显示多少条") @RequestParam(value = "size", defaultValue = "10") int size) {

        Map<String,Object> pageMap = couponService.pageCouponActivity(page,size);
        return JsonData.buildSuccess(pageMap);
    }

    @ApiOperation("领取促销劵")
    @GetMapping("/receive/promotion/{coupon_id}")
    public JsonData receivePromotionCoupon(@ApiParam(value = "优惠券id",required = true) @PathVariable("coupon_id")long couponId){
        JsonData jsonData = couponService.receiveCoupon(couponId, CouponEnum.CouponCategoryEnum.PROMOTION.getCategory());

        return jsonData;
    }

    /**
     * 新用户注册发放优惠券接口
     * @return
     */
    @ApiOperation("RPC-新用户注册接口")
    @PostMapping("/new_user_coupon")
    public JsonData addNewUserCoupon( @ApiParam("用户对象") @RequestBody NewUserCouponRequest newUserCouponRequest ){

        JsonData jsonData = couponService.initNewUserCoupon(newUserCouponRequest);

        return jsonData;
    }

}
