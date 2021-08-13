package com.gosenor.controller;

import com.gosenor.request.CartItemRequest;
import com.gosenor.service.CartService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-12 14:21
 */
@Api(tags = "购物车模块")
@RestController
@RequestMapping("/api/cart/v1")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiOperation(value = "添加到购物车",notes = "buyNum为追加数量")
    @PostMapping("add")
    public JsonData addToCart( @ApiParam("购物项") @RequestBody @Valid CartItemRequest cartItemRequest){
        cartService.addToCart(cartItemRequest);
        return JsonData.buildSuccess();
    }



    @ApiOperation("修改购物车数量")
    @PostMapping("change")
    public JsonData changeItemNum( @ApiParam("购物项") @RequestBody @Valid CartItemRequest cartItemRequest){
        cartService.changeItemNum(cartItemRequest);
        return JsonData.buildSuccess();
    }




    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public JsonData cleanMyCart(){
        cartService.clear();
        return JsonData.buildSuccess();
    }



    @ApiOperation("查看我的购物车")
    @GetMapping("/mycart")
    public JsonData findMyCart(){
        CartVO cartVO = cartService.getMyCart();
        return JsonData.buildSuccess(cartVO);
    }



    @ApiOperation("删除购物项")
    @DeleteMapping("/delete/{product_id}")
    public JsonData deleteItem( @ApiParam(value = "商品id",required = true)@PathVariable("product_id")long productId ){
        cartService.deleteItem(productId);
        return JsonData.buildSuccess();
    }
}
