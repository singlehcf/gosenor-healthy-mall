package com.gosenor.controller;


import com.gosenor.enums.BizCodeEnum;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.model.LoginUser;
import com.gosenor.request.AddressAddRequest;
import com.gosenor.service.AddressService;

import com.gosenor.utils.JsonData;
import com.gosenor.vo.AddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2021-01-26
 */
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/address/v1/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("根据id获取地址详情")
    @GetMapping("/find/{address_id}")
    public JsonData<AddressVO> detail(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id") long addressId){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressVO addressVO = addressService.detail(addressId,loginUser.getId());
        return JsonData.buildSuccess(addressVO);
    }

    @ApiOperation("获取默认地址")
    @GetMapping("/find/default")
    public JsonData<AddressVO> getDefaultAddress(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressVO addressVO = addressService.getUserDefaultAddress(loginUser.getId());
        return JsonData.buildSuccess(addressVO);
    }

    @ApiOperation("新增收货地址")
    @PostMapping("/add")
    public JsonData add(@ApiParam("地址对象") @RequestBody AddressAddRequest addressAddRequest){
        addressService.add(addressAddRequest);
        return JsonData.buildSuccess();
    }

    @ApiOperation("获取用户所有地址")
    @GetMapping("/list")
    public JsonData<List<AddressVO>> getUserAllAddress(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        List<AddressVO> addressVos = addressService.getUserAddress(loginUser.getId());
        return JsonData.buildSuccess(addressVos);
    }

    @ApiOperation("删除指定收货地址")
    @DeleteMapping("/del/{address_id}")
    public JsonData del(@PathVariable("address_id") Long addressId){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        int rows = addressService.del(addressId,loginUser.getId());
        return rows == 1 ? JsonData.buildSuccess(): JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }
}

