package com.gosenor.controller;

import com.gosenor.request.LockProductRequest;
import com.gosenor.service.ProductService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: gosenor-healthy-mall
 * @description: 商品模块
 * @author: hcf
 * @create: 2021-08-11 13:58
 */
@Api(tags = "商品模块")
@RestController
@RequestMapping("/api/product/v1")
public class ProductController {
    @Autowired
    private ProductService productService;


    @ApiOperation("分页查询商品列表")
    @GetMapping("page")
    public JsonData pageProductList(
            @ApiParam(value = "当前页")  @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "每页显示多少条") @RequestParam(value = "size", defaultValue = "10") int size
    ){

        Map<String,Object> pageResult = productService.page(page,size);

        return JsonData.buildSuccess(pageResult);


    }



    @ApiOperation("商品详情")
    @GetMapping("/detail/{product_id}")
    public JsonData detail(@ApiParam(value = "商品id",required = true) @PathVariable("product_id") long productId){

        ProductVO productVO = productService.findDetailById(productId);
        return JsonData.buildSuccess(productVO);
    }

    /**
     * 商品库存锁定
     * @return
     */
    @ApiOperation("商品库存锁定")
    @PostMapping("lock_products")
    public JsonData lockProducts(@ApiParam("商品库存锁定") @RequestBody LockProductRequest lockProductRequest){


        JsonData jsonData = productService.lockProductStock(lockProductRequest);

        return jsonData;
    }


}
