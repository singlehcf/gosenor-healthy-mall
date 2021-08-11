package com.gosenor.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.gosenor.component.MailService;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.SendCodeEnum;
import com.gosenor.service.NotifyService;
import com.gosenor.utils.CommonUtil;
import com.gosenor.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-22 09:46
 */
@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/user/v1")
@Slf4j
public class NotifyController {
    private static final long CAPTCHA_CODE_EXPIRED = 60 * 1000 * 10;

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NotifyService notifyService;


    @ApiOperation("获取图形验证码")
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
        String capText = captchaProducer.createText();
        log.info("图形验证码:{}",capText);
        String cacheKey = this.getCacheKey(request);
        //保存
        redisTemplate.opsForValue().set(cacheKey,capText,CAPTCHA_CODE_EXPIRED, TimeUnit.MILLISECONDS);
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream outputStream = null;
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "create_date-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            outputStream = response.getOutputStream();
            ImageIO.write(bi,"jpg",outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            log.error("获取验证码失败:{}",e);
        }
    }

    /**
     * 发送验证码
     * 1、匹配图形验证码是否正常
     * 2、发送验证码
     *
     * @param to
     * @param captcha
     * @return
     */
    @ApiOperation("发送邮箱注册验证码")
    @GetMapping("/send_code")
    public JsonData sendRegisterCode(@RequestParam(value = "to",required = true) String to,
                                     @RequestParam(value = "captcha",required = true) String captcha,
                                     HttpServletRequest request){

        String key = this.getCacheKey(request);
        String cacheCaptcha = redisTemplate.opsForValue().get(key);
        //匹配图形验证码是否一样
        if(captcha !=null && cacheCaptcha !=null && captcha.equalsIgnoreCase(cacheCaptcha)){
            //成功
            redisTemplate.delete(key);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER,to);
            return jsonData;
        }else{
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }

    private String getCacheKey(HttpServletRequest request){
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String cacheKey = "healthy-user-service:captcha:"+CommonUtil.MD5(ip+userAgent);
        log.info("ip={}",ip);
        log.info("userAgent={}",userAgent);
        log.info("cacheKey={}",cacheKey);
        return cacheKey;
    }
}
