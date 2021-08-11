package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.mapper.CouponRecordMapper;
import com.gosenor.model.CouponRecordDO;
import com.gosenor.model.LoginUser;
import com.gosenor.service.CouponRecordService;
import com.gosenor.service.CouponService;
import com.gosenor.vo.CouponRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {
        if (couponRecordDO == null){
            return null;
        }
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
