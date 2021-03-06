package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gosenor.config.RabbitMQConfig;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.CouponEnum;
import com.gosenor.enums.ProductOrderStateEnum;
import com.gosenor.enums.StockTaskStateEnum;
import com.gosenor.exception.BizException;
import com.gosenor.feign.OrderFeignService;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.mapper.CouponRecordMapper;
import com.gosenor.mapper.CouponTaskMapper;
import com.gosenor.model.CouponRecordDO;
import com.gosenor.model.CouponRecordMessage;
import com.gosenor.model.CouponTaskDO;
import com.gosenor.model.LoginUser;
import com.gosenor.request.LockCouponRecordRequest;
import com.gosenor.service.CouponRecordService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.CouponRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-08-11 10:55
 */
@Service
@Slf4j
public class CouponRecordServiceImpl implements CouponRecordService {

    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Autowired
    private CouponTaskMapper couponTaskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private OrderFeignService orderFeignService;

    @Override
    public Map<String, Object> page(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //??????????????????
        Page<CouponRecordDO> pageInfo = new Page<>(page,size);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",loginUser.getId());
        queryWrapper.orderByDesc("create_time");
        IPage<CouponRecordDO> iPage = couponRecordMapper.selectPage(pageInfo,queryWrapper);
        List<CouponRecordDO> couponRecordDOS = iPage.getRecords();
        List<CouponRecordVO> couponRecordVOS = couponRecordDOS.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        Map<String,Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record",iPage.getTotal());
        pageMap.put("total_page",iPage.getPages());
        pageMap.put("current_data",couponRecordVOS);
        return pageMap;
    }

    @Override
    public CouponRecordVO findUserCouponById(long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId).eq("user_id",loginUser.getId()));

        return beanProcess(couponRecordDO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData lockCouponRecords(LockCouponRecordRequest recordRequest) {
        //todo:??????????????????
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        List<Long> lockCouponRecordIds = recordRequest.getLockCouponRecordIds();
        String orderOutTradeNo = recordRequest.getOrderOutTradeNo();
        if (lockCouponRecordIds == null || orderOutTradeNo == null){
            return JsonData.buildResult(BizCodeEnum.NO_RARAM);
        }
        //?????????????????????????????????
        int updateNum = couponRecordMapper.lockUseStateBatch(loginUser.getId(),
                CouponEnum.CouponUseStateEnum.USED.name(),
                CouponEnum.CouponUseStateEnum.NEW.name(),
                lockCouponRecordIds);
        //?????????????????????????????????
        List<CouponTaskDO> couponTaskDOList = lockCouponRecordIds.stream().map(obj -> {
            CouponTaskDO couponTaskDO = new CouponTaskDO();
            couponTaskDO.setCouponRecordId(obj);
            couponTaskDO.setLockState(StockTaskStateEnum.LOCK.name());
            couponTaskDO.setCreateTime(new Date());
            couponTaskDO.setOutTradeNo(orderOutTradeNo);
            return couponTaskDO;
        }).collect(Collectors.toList());
        int insertNum = couponTaskMapper.insertBeatch(couponTaskDOList);

        log.info("?????????????????????updateRows={}",updateNum);
        log.info("?????????????????????task insertRows={}",insertNum);
        //??????????????????
        if(lockCouponRecordIds.size() == updateNum && insertNum==updateNum){
            //??????????????????
            for (CouponTaskDO couponTaskDO : couponTaskDOList){
                CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
                couponRecordMessage.setTaskId(couponTaskDO.getId());
                couponRecordMessage.setOutTradeNo(couponTaskDO.getOutTradeNo());
                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getCouponReleaseDelayRoutingKey(),couponRecordMessage);
                log.info("?????????????????????????????????: {}",couponRecordMessage.toString());
            }
            return JsonData.buildSuccess();
        }else {
            throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public boolean releaseCouponRecord(CouponRecordMessage recordMessage) {
        //???????????????task
        CouponTaskDO couponTaskDO = couponTaskMapper.selectById(recordMessage.getTaskId());
        if (couponTaskDO == null){
            return true;
        }
        //??????task?????????????????????
        if (!StockTaskStateEnum.LOCK.name().equalsIgnoreCase(couponTaskDO.getLockState())){
            return true;
        }

        //????????????????????????????????????
        JsonData orderJsonDate = orderFeignService.queryProductOrderState(recordMessage.getOutTradeNo());
        if (!orderJsonDate.isSuccess()){
            log.warn("???????????????????????????????????? jsondata:{}",orderJsonDate);
            return false;
        }
        String state = orderJsonDate.getData().toString();
        if (ProductOrderStateEnum.NEW.name().equalsIgnoreCase(state)){
            //?????????NEW??????????????????????????????????????????????????????
            log.warn("???????????????NEW,????????????????????????????????????:{}",recordMessage);
            return false;
        }
        //??????????????????????????????
        if (ProductOrderStateEnum.PAY.name().equalsIgnoreCase(state)){
            //?????????????????????
            couponTaskDO.setLockState(StockTaskStateEnum.FINISH.name());
        }else {
            //??????????????????????????????????????????????????????,??????task?????????CANCEL,??????????????????????????????NEW
            couponTaskDO.setLockState(StockTaskStateEnum.CANCEL.name());
            //????????????????????????NEW??????
            couponRecordMapper.updateUseState(couponTaskDO.getCouponRecordId(),CouponEnum.CouponUseStateEnum.NEW.name());
        }
        couponTaskMapper.updateById(couponTaskDO);
        return true;
    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {
        if (couponRecordDO == null){
            return null;
        }
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
