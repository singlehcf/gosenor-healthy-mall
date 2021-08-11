package com.gosenor.controller;

import com.gosenor.enums.BizCodeEnum;
import com.gosenor.service.CouponRecordService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CouponRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-11 10:53
 */
@Api(tags = "优惠卷记录")
@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {
    @Autowired
    private CouponRecordService couponRecordService;



    @ApiOperation("分页查询个人优惠券")
    @GetMapping("page")
    public JsonData page(@ApiParam(value = "当前页")  @RequestParam(value = "page", defaultValue = "1") int page,
                         @ApiParam(value = "每页显示多少条") @RequestParam(value = "size", defaultValue = "10") int size){

        Map<String,Object> pageResult = couponRecordService.page(page,size);

        return JsonData.buildSuccess(pageResult);
    }



    @ApiOperation("查询优惠券记录详情")
    @GetMapping("detail/{record_id}")
    public JsonData getCouponRecordDetail(@ApiParam(value = "记录id")  @PathVariable("record_id") long recordId){

        CouponRecordVO couponRecordVO = couponRecordService.findUserCouponById(recordId);

        return couponRecordVO == null ? JsonData.buildResult(BizCodeEnum.COUPON_NO_EXITS):JsonData.buildSuccess(couponRecordVO);

    }

}
