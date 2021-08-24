package com.gosenor.mapper;

import com.gosenor.model.CouponTaskDO;
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
 * @since 2021-08-24
 */
@Repository
public interface CouponTaskMapper extends BaseMapper<CouponTaskDO> {
    /**
     * 批量新增
     * @param couponTaskDOList
     * @return
     */
    int insertBeatch(@Param("couponTaskList") List<CouponTaskDO> couponTaskDOList);
}
