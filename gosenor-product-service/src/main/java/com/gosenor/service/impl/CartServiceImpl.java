package com.gosenor.service.impl;

import com.gosenor.constant.CacheKey;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.exception.BizException;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.model.LoginUser;
import com.gosenor.request.CartItemRequest;
import com.gosenor.service.CartService;
import com.gosenor.service.ProductService;
import com.gosenor.utils.JsonUtil;
import com.gosenor.vo.CartItemVO;
import com.gosenor.vo.CartVO;
import com.gosenor.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description: 购物车
 * @author: hcf
 * @create: 2021-08-12 14:24
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    @Override
    public void addToCart(CartItemRequest cartItemRequest) {
        int buyNum = cartItemRequest.getBuyNum();
        long productId = cartItemRequest.getProductId();
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();
        Object cartItemObjet = myCart.get(cartItemRequest.getProductId());
        String result = "";
        if(cartItemObjet != null){
            result =  (String)cartItemObjet;
        }
        if(StringUtils.isBlank(result)){
            //不存在则新建一个商品
            CartItemVO cartItemVO = new CartItemVO();

            ProductVO productVO = productService.findDetailById(productId);
            if(productVO == null){throw new BizException(BizCodeEnum.CART_FAIL);}

            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setBuyNum(buyNum);
            cartItemVO.setProductId(productId);
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());
            myCart.put(productId, JsonUtil.transferToJson(cartItemVO));

        }else {
            //存在商品，修改数量
            CartItemVO cartItem = JsonUtil.jsonToTransfer(result,CartItemVO.class);
            cartItem.setBuyNum(cartItem.getBuyNum()+buyNum);
            myCart.put(productId,JsonUtil.transferToJson(cartItem));
        }

    }

    @Override
    public void changeItemNum(CartItemRequest cartItemRequest) {
        int buyNum = cartItemRequest.getBuyNum();
        long productId = cartItemRequest.getProductId();
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();
        Object cartItemObjet = myCart.get(cartItemRequest.getProductId());
        if(cartItemObjet == null){
            throw new BizException(BizCodeEnum.CART_FAIL);
        }
        String result =  (String)cartItemObjet;
        CartItemVO cartItem = JsonUtil.jsonToTransfer(result,CartItemVO.class);
        cartItem.setBuyNum(buyNum);
        myCart.put(productId,JsonUtil.transferToJson(cartItem));

    }

    @Override
    public void clear() {
        String cartKey = this.getCartKey();
        redisTemplate.delete(cartKey);
    }

    @Override
    public CartVO getMyCart() {
        //获取全部购物项
        List<CartItemVO> cartItemVOList = buildCartItem(false);

        CartVO cartVO = new CartVO();
        cartVO.setCartItems(cartItemVOList);
        return cartVO;
    }

    /**
     * 获取最新的购物项，
     * @param latestPrice 是否获取最新价格
     * @return
     */
    private List<CartItemVO> buildCartItem(boolean latestPrice) {
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();
        List<Object> itemList = myCart.values();
        List<CartItemVO> cartItemVOList = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        itemList.stream().forEach(ojb ->{
            CartItemVO cartItemVO = JsonUtil.jsonToTransfer((String) ojb,CartItemVO.class);
            cartItemVOList.add(cartItemVO);
            productIds.add(cartItemVO.getProductId());
        });

        if (latestPrice){
            setProductLatestPrice(cartItemVOList,productIds);
        }

        return cartItemVOList;
    }

    /**
     * 设置商品最新价格
     * @param cartItemVOList
     * @param productIds
     */
    private void setProductLatestPrice(List<CartItemVO> cartItemVOList, List<Long> productIds) {
        List<ProductVO> productVOS = productService.findProductsByIds(productIds);

        Map<Long,ProductVO> productVOMap = productVOS.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));
        cartItemVOList.stream().forEach(item ->{
            ProductVO productVO = productVOMap.get(item.getProductId());
            item.setAmount(productVO.getAmount());
            item.setProductImg(productVO.getCoverImg());
            item.setProductTitle(productVO.getTitle());
        });
    }

    @Override
    public void deleteItem(long productId) {
        BoundHashOperations<String,Object,Object> myCart = getMyCartOps();
        myCart.delete(productId);
    }

    /**
     * 抽取我的购物车，通用方法
     * @return
     */
    private BoundHashOperations<String, Object, Object> getMyCartOps() {
        String key = getCartKey();
        BoundHashOperations<String,Object,Object> cartObject = redisTemplate.boundHashOps(key);
        return cartObject;
    }

    /**
     * 购物车 key
     * @return
     */
    private String getCartKey() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String key = String.format(CacheKey.CART_KEY,loginUser.getId());
        return key;
    }
}
