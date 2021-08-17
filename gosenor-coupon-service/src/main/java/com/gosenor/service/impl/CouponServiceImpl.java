package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.CouponEnum;
import com.gosenor.exception.BizException;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.mapper.CouponMapper;
import com.gosenor.mapper.CouponRecordMapper;
import com.gosenor.model.CouponDO;
import com.gosenor.model.CouponRecordDO;
import com.gosenor.model.LoginUser;
import com.gosenor.request.NewUserCouponRequest;
import com.gosenor.service.CouponService;
import com.gosenor.utils.CommonUtil;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CouponVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-02 13:50
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Map<String, Object> pageCouponActivity(int page, int size) {
        Page<CouponDO> couponDOPage = new Page<>(page,size);
        QueryWrapper<CouponDO> queryWrapper = new QueryWrapper();
        //促销券
        queryWrapper.eq("category", CouponEnum.CouponCategoryEnum.PROMOTION.getCategory());
        //已发布
        queryWrapper.eq("publish",CouponEnum.CouponPublishEnum.PUBLISH);
        queryWrapper.orderByDesc("create_time");
        IPage<CouponDO> iPage = couponMapper.selectPage(couponDOPage,queryWrapper);

        Map<String, Object> pageMap = new
                HashMap<>(3);
        pageMap.put("total_record",
                couponDOPage.getTotal());
        pageMap.put("total_page",
                couponDOPage.getPages());
        pageMap.put("current_data",
                couponDOPage.getRecords().stream().map(obj ->
                        beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * 1、获取优惠卷
     * 2、检测优惠卷
     * 3、扣减库存
     * 4、添加优惠卷记录活动
     * @param couponId
     * @param couponCategory
     * @return
     */
    @Transactional(rollbackFor=Exception.class,propagation= Propagation.REQUIRED)
    @Override
    public JsonData receiveCoupon(long couponId, String couponCategory) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String lockKey = "lock:coupon:"+couponId;
        //加锁
        RLock rLock = redissonClient.getLock(lockKey);
        log.info("领劵接口加锁成功:{}",Thread.currentThread().getId());
        try {
            CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
                    .eq("category",couponCategory)
                    .eq("id",couponId)
                    .eq("publish",CouponEnum.CouponPublishEnum.PUBLISH.name()));
            if (couponDO == null){
                throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
            }
            this.checkCoupon(couponDO,loginUser.getId());
            int rows = couponMapper.reduceStock(couponId);
            if (rows == 1) {
                CouponRecordDO couponRecordDO = new CouponRecordDO();
                BeanUtils.copyProperties(couponDO,couponRecordDO);
                couponRecordDO.setUserId(loginUser.getId());
                couponRecordDO.setCouponId(couponId);
                couponRecordDO.setCreateTime(new Date());
                couponRecordDO.setUserName(loginUser.getName());
                couponRecordDO.setUseState(CouponEnum.CouponStateEnum.NEW.name());
                //库存扣减成功才保存记录
                couponRecordMapper.insert(couponRecordDO);

            } else {
                log.warn("发放优惠券失败:{},用户:{}", couponDO, loginUser);

                throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
            }
        }finally {
            rLock.unlock();
            log.info("解锁成功");
        }
        return JsonData.buildSuccess();
    }

    /**
     * 用户微服务调用的时候，没传递token
     * 本地直接调用发放优惠券的方法，需要构造一个登录用户存储在threadlocal
     * @param newUserCouponRequest
     * @return
     */
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
    @Override
    public JsonData initNewUserCoupon(NewUserCouponRequest newUserCouponRequest) {
        LoginUser loginUser = LoginUser.builder().id(newUserCouponRequest.getUserId()).name(newUserCouponRequest.getName()).build();
        LoginInterceptor.threadLocal.set(loginUser);

        List<CouponDO> couponDOList = couponMapper.selectList(new QueryWrapper<CouponDO>()
                .eq("category",CouponEnum.CouponCategoryEnum.NEW_USER.getCategory()));
        //获取所有注册活动
        for(CouponDO couponDO : couponDOList){
            //幂等操作，调用需要加锁
            this.receiveCoupon(couponDO.getId(),CouponEnum.CouponCategoryEnum.NEW_USER.getCategory());
        }
        return JsonData.buildSuccess();
    }

    private void checkCoupon(CouponDO couponDO, Long userId) {
        if (couponDO == null || userId == null){
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        //判断库存
        if (couponDO.getStock() <= 0){
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        //判断是否是否发布状态
        if(!couponDO.getPublish().equals(CouponEnum.CouponPublishEnum.PUBLISH.name())){
            throw new BizException(BizCodeEnum.COUPON_GET_FAIL);
        }
        //判断时间是否在有效期
        long nowTime = CommonUtil.getCurrentTimestamp();
        long startTime = couponDO.getStartTime().getTime();
        long endTime = couponDO.getEndTime().getTime();
        if (nowTime < startTime || nowTime > endTime){
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }
        //判断用户领取次数
        int coponNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
                .eq("user_id",userId)
                .eq("coupon_id",couponDO.getId()));
        if (coponNum >= couponDO.getUserLimit()){
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }
    }

    private CouponVO beanProcess(CouponDO couponDO) {
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(couponDO,couponVO);
        return couponVO;
    }
}
