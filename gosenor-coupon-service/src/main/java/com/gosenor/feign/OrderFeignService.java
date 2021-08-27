package com.gosenor.feign;

import com.gosenor.utils.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "healthy-order-server")
public interface OrderFeignService {

    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    @GetMapping("/api/order/v1/query_state")
    JsonData<String> queryProductOrderState(@RequestParam("out_trade_no") String outTradeNo);


}
