package com.gosenor.service.impl;

import com.gosenor.component.MailService;
import com.gosenor.constant.CacheKey;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.SendCodeEnum;
import com.gosenor.service.NotifyService;
import com.gosenor.utils.CheckUtil;
import com.gosenor.utils.CommonUtil;
import com.gosenor.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-22 15:12
 */
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    private MailService mailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 验证码的标题
     */
    private static final String SUBJECT= "大健康商城验证码";

    /**
     * 验证码的内容
     */
    private static final String CONTENT= "您的验证码是%s,有效时间是10分钟,打死也不要告诉任何人";

    /**
     * 验证码10分钟有效
     */
    private static final int CODE_EXPIRED = 60 * 1000 * 10;
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        String checkCodeKey = String.format(CacheKey.CHECK_CODE_KEY,sendCodeEnum.name(),to);
        //获取缓存
        String cacheValue = redisTemplate.opsForValue().get(checkCodeKey);
        //如果不为空判断60秒内
        if (StringUtils.isNotBlank(cacheValue)){
            Long ttl = Long.parseLong(cacheValue.split("_")[1]);
            if (CommonUtil.getCurrentTimestamp()-ttl < 1000*60){
                log.info("重复发送验证码,时间间隔: 23{} 秒",(CommonUtil.getCurrentTimestamp()-ttl)/1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String code = CommonUtil.getRandomCode(6);
        String checkCodeValue = code+"_"+CommonUtil.getCurrentTimestamp();
        redisTemplate.opsForValue().set(checkCodeKey,checkCodeValue,CODE_EXPIRED, TimeUnit.MILLISECONDS);
        if(CheckUtil.isEmail(to)){
            //邮箱验证码
            mailService.sendSimpleMail(to,SUBJECT,String.format(CONTENT,code));
            return JsonData.buildSuccess();
        }else if(CheckUtil.isPhone(to)){
            //短信验证码 todo:
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }

    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code) {
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY,sendCodeEnum.name(),to);
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheValue)){
            String cacheCode = cacheValue.split("_")[0];
            if (cacheCode.equals(code)){
                //删除验证码
                redisTemplate.delete(cacheKey);
                return true;
            }
        }
        return false;
   }

}
