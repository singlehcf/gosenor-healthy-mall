package com.gosenor.service;

import com.gosenor.request.CartItemRequest;
import com.gosenor.vo.CartVO;

public interface CartService {
    /**
     *  添加到购物车
     * @param cartItemRequest
     */
    void addToCart(CartItemRequest cartItemRequest);

    /**
     * 修改购物车数量
     * @param cartItemRequest
     */
    void changeItemNum(CartItemRequest cartItemRequest);

    /**
     * 情况购物车
     */
    void clear();

    /**
     * 获取我的购物车
     * @return
     */
    CartVO getMyCart();

    /**
     * 删除购物项
     * @param productId
     */
    void deleteItem(long productId);
}
