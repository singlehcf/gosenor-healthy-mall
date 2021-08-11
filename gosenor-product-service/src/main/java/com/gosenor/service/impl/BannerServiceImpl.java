package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gosenor.mapper.BannerMapper;
import com.gosenor.model.BannerDO;
import com.gosenor.service.BannerService;
import com.gosenor.vo.BannerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description: 轮播图
 * @author: hcf
 * @create: 2021-08-11 11:49
 */
@Service
@Slf4j
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public Object list() {
        List<BannerDO> bannerDOS = bannerMapper.selectList(new QueryWrapper<BannerDO>().orderByAsc("weight"));
        List<BannerVO> bannerVOS = bannerDOS.stream().map(obj ->{
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(obj,bannerVO);
            return bannerVO;
        }).collect(Collectors.toList());
        return bannerVOS;
    }
}
