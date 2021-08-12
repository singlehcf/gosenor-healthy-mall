package com.gosenor.service;

import com.gosenor.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Map<String, Object> page(int page, int size);

    ProductVO findDetailById(long productId);

    List<ProductVO> findProductsByIds(List<Long> productIds);
}
