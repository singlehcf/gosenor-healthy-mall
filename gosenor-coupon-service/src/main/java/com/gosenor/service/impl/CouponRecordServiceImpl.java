package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.CouponEnum;
import com.gosenor.enums.StockTaskStateEnum;
import com.gosenor.exception.BizException;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.mapper.CouponRecordMapper;
import com.gosenor.mapper.CouponTaskMapper;
import com.gosenor.model.CouponRecordDO;
import com.gosenor.model.CouponTaskDO;
import com.gosenor.model.LoginUser;
import com.gosenor.request.LockCouponRecordRequest;
import com.gosenor.service.CouponRecordService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CouponRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-11 10:55
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {

    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Autowired
    private CouponTaskMapper couponTaskMapper;

    @Override
    public Map<String, Object> page(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //封装分页信息
        Page<CouponRecordDO> pageInfo = new Page<>(page,size);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",loginUser.getId());
        queryWrapper.orderByDesc("create_time");
        IPage<CouponRecordDO> iPage = couponRecordMapper.selectPage(pageInfo,queryWrapper);
        List<CouponRecordDO> couponRecordDOS = iPage.getRecords();
        List<CouponRecordVO> couponRecordVOS = couponRecordDOS.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",iPage.getTotal());
        pageMap.put("total_page",iPage.getPages());
        pageMap.put("current_data",couponRecordVOS);
        return pageMap;
    }

    @Override
    public CouponRecordVO findUserCouponById(long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId).eq("user_id",loginUser.getId()));

        return beanProcess(couponRecordDO);
    }


    @Override
    public JsonData lockCouponRecords(LockCouponRecordRequest recordRequest) {
        //todo:是否使用事务
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        List<Long> lockCouponRecordIds = recordRequest.getLockCouponRecordIds();
        String orderOutTradeNo = recordRequest.getOrderOutTradeNo();
        if (lockCouponRecordIds == null || orderOutTradeNo == null){
            return JsonData.buildResult(BizCodeEnum.NO_RARAM);
        }
        //批量修改优惠券使用状态
        int updateNum = couponRecordMapper.lockUseStateBatch(loginUser.getId(),
                CouponEnum.CouponUseStateEnum.USED.name(),
                CouponEnum.CouponUseStateEnum.NEW.name(),
                lockCouponRecordIds);
        //创建优惠券记录锁定任务
        List<CouponTaskDO> couponTaskDOList = lockCouponRecordIds.stream().map(obj -> {
            CouponTaskDO couponTaskDO = new CouponTaskDO();
            couponTaskDO.setCouponRecordId(obj);
            couponTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
            couponTaskDO.setCreateTime(new Date());
            couponTaskDO.setOutTradeNo(orderOutTradeNo);
            return couponTaskDO;
        }).collect(Collectors.toList());
        int insertNum = couponTaskMapper.insertBeatch(couponTaskDOList);
        //投递定时任务
        if(lockCouponRecordIds.size() == updateNum && insertNum==updateNum){
            //发送延迟消息

            return JsonData.buildSuccess();
        }else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }
    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {
        if (couponRecordDO == null){
            return null;
        }
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
