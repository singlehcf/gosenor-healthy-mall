package com.gosenor.service;

import com.gosenor.model.AddressDO;
import com.gosenor.request.AddressAddRequest;
import com.gosenor.vo.AddressVO;

import java.util.List;

public interface AddressService {
    /**
     * 获取地址详情
     * @param id 地址id
     * @return
     */
    public AddressVO detail(Long id);

    /**
     * 获取用户地址详情
     * @param id 地址id
     * @return
     */
    public AddressVO detail(Long id,Long userId);

    public void add(AddressAddRequest addressAddRequest);

    /**
     * 获取用户所有地址
     * @param userId 用户id
     * @return
     */
    public List<AddressVO> getUserAddress(Long userId);

    /**
     * 获取用户默认地址
     * @param userId
     * @return
     */
    public AddressVO getUserDefaultAddress(Long userId);

    /**
     * 删除地址
     * @param addressId
     * @return
     */
    public int del(Long addressId,Long userId);
}
