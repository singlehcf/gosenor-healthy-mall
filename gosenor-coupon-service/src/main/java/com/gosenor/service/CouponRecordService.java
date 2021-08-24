package com.gosenor.service;

import com.gosenor.request.LockCouponRecordRequest;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CouponRecordVO;

import java.util.Map;

public interface CouponRecordService {
    /**
     * 分页获取用户优惠卷
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> page(int page, int size);

    /**
     * 获取用户优惠卷详情
     * @param recordId
     * @return
     */
    CouponRecordVO findUserCouponById(long recordId);

    /**
     * 锁定优惠卷记录
     * @param recordRequest
     * @return
     */
    JsonData lockCouponRecords(LockCouponRecordRequest recordRequest);
}
