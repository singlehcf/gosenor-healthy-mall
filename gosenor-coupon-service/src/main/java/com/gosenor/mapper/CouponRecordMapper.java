package com.gosenor.mapper;

import com.gosenor.model.CouponRecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hcf
 * @since 2021-08-02
 */
@Repository
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {

    /**
     * 批量修改优惠卷使用状态
     * @param userId 用户id
     * @param useState 使用状态
     * @param oldUseState 之前使用状态
     * @param lockCouponRecordIds 优惠卷id
     */
    int lockUseStateBatch(@Param("userId") Long userId,@Param("useState") String useState,@Param("oldUseState") String oldUseState,@Param("lockCouponRecordIds") List<Long> lockCouponRecordIds);
}
