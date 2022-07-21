package com.gosenor.service;

import com.gosenor.enums.CouponEnum;
import com.gosenor.request.NewUserCouponRequest;
import com.gosenor.utils.JsonData;

import java.util.Map;

public interface CouponService {

    /**
     * 分页获取促销卷优惠活动
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> pageCouponActivity(int page, int size);

    /**
     * 领取优惠卷
     * @param couponId
     * @param couponCategory
     * @return
     */
    public JsonData receiveCoupon(long couponId,String couponCategory);

    /**
     * 新用户注册发放优惠券
     * @param newUserCouponRequest
     * @return
     */
    JsonData initNewUserCoupon(NewUserCouponRequest newUserCouponRequest);
}
