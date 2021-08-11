package com.gosenor.mapper;

import com.gosenor.model.CouponDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hcf
 * @since 2021-08-02
 */
@Repository
public interface CouponMapper extends BaseMapper<CouponDO> {

    public int reduceStock(long couponId);
}
