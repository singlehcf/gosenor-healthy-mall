package com.gosenor.feign;

import com.gosenor.request.NewUserCouponRequest;
import com.gosenor.utils.JsonData;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "healthy-coupon-service")
public interface CouponFeignService {

    /**
     * 新用户注册发放优惠券
     * @param newUserCouponRequest
     * @return
     */
    @PostMapping("/api/coupon/v1/new_user_coupon")
    JsonData addNewUserCoupon(@ApiParam("用户对象") @RequestBody NewUserCouponRequest newUserCouponRequest );

}
