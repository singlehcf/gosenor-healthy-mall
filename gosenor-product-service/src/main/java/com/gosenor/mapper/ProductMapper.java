package com.gosenor.mapper;

import com.gosenor.model.ProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hcf
 * @since 2021-08-11
 */
@Repository
public interface ProductMapper extends BaseMapper<ProductDO> {

    /**
     * 锁定商品库存
     * @param productId 商品id
     * @param buyNum 购买数量
     * @return
     */
    int lockProductStock(@Param("productId") long productId, @Param("buyNum") int buyNum);
}
