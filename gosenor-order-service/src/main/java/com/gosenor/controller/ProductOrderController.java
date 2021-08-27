package com.gosenor.controller;

import com.gosenor.enums.ClientTypeEnum;
import com.gosenor.enums.ProductOrderPayTypeEnum;
import com.gosenor.request.ConfirmOrderRequest;
import com.gosenor.service.ProductOrderService;
import com.gosenor.utils.CommonUtil;
import com.gosenor.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-13 14:19
 */
@Api("订单模块")
@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {
    @Autowired
    private ProductOrderService productOrderService;

    @ApiOperation("提交订单")
    @PostMapping("confirm")
    public void confirmOrder(@ApiParam("订单对象") @RequestBody ConfirmOrderRequest orderRequest, HttpServletResponse response){
        JsonData jsonData = productOrderService.confirmOrder(orderRequest);
        if (!jsonData.isSuccess()){
            log.error("创建订单失败{}",jsonData.toString());
            CommonUtil.sendJsonMessage(response,jsonData);
        }
        String client = orderRequest.getClientType();
        String payType = orderRequest.getPayType();

        //如果是支付宝网页支付，都是跳转网页，APP除外
        if(payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALIPAY.name())){

            log.info("创建支付宝订单成功:{}",orderRequest.toString());

            if(client.equalsIgnoreCase(ClientTypeEnum.H5.name())){
                writeData(response,jsonData);

            }else if(client.equalsIgnoreCase(ClientTypeEnum.APP.name())){
                //APP SDK支付  TODO
            }

        } else if(payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT.name())){
            //微信支付 TODO
        }
    }


    @GetMapping("query_state")
    public JsonData<String> queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo){
        String state = productOrderService.queryProductOrderState(outTradeNo);
        return JsonData.buildSuccess(state);
    }

    private void writeData(HttpServletResponse response, JsonData jsonData) {

        try {
            response.setContentType("text/html;charset=UTF8");
            response.getWriter().write(jsonData.getData().toString());
            response.getWriter().flush();
            response.getWriter().close();
        }catch (IOException e){
            log.error("写出Html异常：{}",e);
        }

    }
}
