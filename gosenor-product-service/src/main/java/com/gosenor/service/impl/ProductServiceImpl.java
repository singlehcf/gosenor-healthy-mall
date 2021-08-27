package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gosenor.config.RabbitMQConfig;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.StockTaskStateEnum;
import com.gosenor.exception.BizException;
import com.gosenor.mapper.ProductMapper;
import com.gosenor.mapper.ProductTaskMapper;
import com.gosenor.model.ProductDO;
import com.gosenor.model.ProductMessage;
import com.gosenor.model.ProductTaskDO;
import com.gosenor.request.LockProductRequest;
import com.gosenor.request.OrderItemRequest;
import com.gosenor.service.ProductService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-11 13:58
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTaskMapper productTaskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Override
    public Map<String, Object> page(int page, int size) {
        Page<ProductDO> pageInfo = new Page<>(page,size);
        IPage<ProductDO> iPage = productMapper.selectPage(pageInfo,new QueryWrapper<ProductDO>());
        Map<String,Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record",iPage.getTotal());
        pageMap.put("total_page",iPage.getPages());
        pageMap.put("current_data",iPage.getRecords().stream().map(obj->beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    @Override
    public ProductVO findDetailById(long productId) {
        ProductDO productDO = productMapper.selectById(productId);
        ProductVO productVO = beanProcess(productDO);
        return productVO;
    }

    @Override
    public List<ProductVO> findProductsByIds(List<Long> productIds) {
        List<ProductDO> productDOList = productMapper.selectBatchIds(productIds);
        List<ProductVO> productVOList = productDOList.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return productVOList;
    }

    @Override
    public JsonData lockProductStock(LockProductRequest lockProductRequest) {
        String orderOutTradeNo = lockProductRequest.getOrderOutTradeNo();
        List<OrderItemRequest> orderItemList = lockProductRequest.getOrderItemList();
        List<Long> productIdList = orderItemList.stream().map(OrderItemRequest::getProductId).collect(Collectors.toList());
        List<ProductDO> productDOList = productMapper.selectBatchIds(productIdList);
        Map<Long,ProductDO> productDOMap = productDOList.stream().collect(Collectors.toMap(ProductDO::getId, Function.identity()));
        for (OrderItemRequest orderItemRequest : orderItemList){
            int row = productMapper.lockProductStock(orderItemRequest.getProductId(),orderItemRequest.getBuyNum());
            if (row != 1){
                throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
            }
            ProductDO productDO = productDOMap.get(orderItemRequest.getProductId());
            ProductTaskDO productTaskDO = new ProductTaskDO();
            productTaskDO.setBuyNum(orderItemRequest.getBuyNum());
            productTaskDO.setCreateTime(new Date());
            productTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
            productTaskDO.setOutTradeNo(orderOutTradeNo);
            productTaskDO.setProductId(productDO.getId());
            productTaskDO.setProductName(productDO.getTitle());
            productTaskMapper.insert(productTaskDO);

            // 发送MQ延迟消息，介绍商品库存
            ProductMessage productMessage = new ProductMessage();
            productMessage.setOutTradeNo(orderOutTradeNo);
            productMessage.setTaskId(productTaskDO.getId());
            rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getStockReleaseDelayRoutingKey(),productMessage);
            log.info("商品库存锁定信息延迟消息发送成功:{}",productMessage);
        }
        return null;
    }

    private ProductVO beanProcess(ProductDO productDO){
        if (productDO == null){
            return null;
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO,productVO);
        return productVO;
    }
}
