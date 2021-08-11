package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gosenor.enums.AddressEnum;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.mapper.AddressMapper;
import com.gosenor.model.AddressDO;
import com.gosenor.model.LoginUser;
import com.gosenor.request.AddressAddRequest;
import com.gosenor.service.AddressService;
import com.gosenor.vo.AddressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-21 09:43
 */
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressVO detail(Long id) {
        AddressDO addressDO = addressMapper.selectById(id);
        if (addressDO == null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO,addressVO);
        return addressVO;
    }

    @Override
    public AddressVO detail(Long id, Long userId) {
        AddressDO addressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id",id).eq("user_id",userId));
        if (addressDO == null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO,addressVO);
        return addressVO;
    }

    @Override
    public void add(AddressAddRequest addressAddRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = new AddressDO();
        addressDO.setCreateTime(new Date());
        addressDO.setUserId(loginUser.getId());

        BeanUtils.copyProperties(addressAddRequest,addressDO);
        if (addressDO.getDefaultStatus() == null){
            addressDO.setDefaultStatus(AddressEnum.DefaultStatus.DEFAULT_STATUS_0.getStatus());
        }
        //判断该地址是否是默认收获地址
        if (addressDO.getDefaultStatus() == AddressEnum.DefaultStatus.DEFAULT_STATUS_1.getStatus()){
            //是默认地址
            AddressDO defaultAddress = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                    .eq("user_id",addressDO.getUserId())
                    .eq("default_status",addressDO.getDefaultStatus()));
            if (defaultAddress != null){
                defaultAddress.setDefaultStatus(AddressEnum.DefaultStatus.DEFAULT_STATUS_0.getStatus());
                addressMapper.updateById(defaultAddress);
            }
        }

        int row = addressMapper.insert(addressDO);
        log.info("新增收货地址:row={},data={}",row,addressDO);

    }

    @Override
    public List<AddressVO> getUserAddress(Long userId) {
        List<AddressDO> addressDOS = addressMapper.selectList(new QueryWrapper<AddressDO>().eq("user_id",userId));
        if (addressDOS == null || addressDOS.size() == 0){
            return null;
        }
        List<AddressVO> addressVos = addressDOS.stream().map(obj->{
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(obj,addressVO);
            return addressVO;
        }).collect(Collectors.toList());
        return addressVos;
    }

    @Override
    public AddressVO getUserDefaultAddress(Long userId) {
        AddressDO addressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                .eq("user_id",userId)
                .eq("default_status",AddressEnum.DefaultStatus.DEFAULT_STATUS_1.getStatus()));
        if (addressDO == null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO,addressVO);
        return addressVO;
    }

    @Override
    public int del(Long addressId,Long userId) {
        int row = addressMapper.delete(new QueryWrapper<AddressDO>().eq("id",addressId).eq("user_id",userId));
        return row;
    }


}
