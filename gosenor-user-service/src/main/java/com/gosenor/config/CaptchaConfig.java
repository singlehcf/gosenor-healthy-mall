package com.gosenor.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-22 09:14
 */
@Configuration
public class CaptchaConfig {

    @Bean
    @Qualifier("captchaProducer")
    public DefaultKaptcha defaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
//
        properties.setProperty(Constants.KAPTCHA_BORDER, "yes");
//
        properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, "220,220,220");
//
//properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "38,29,12");
//
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "147");
//
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "34");
//
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, "25");
//
//properties.setProperty(Constants.KAPTCHA_SESSON_KEY, "code");
        //验证码个数

        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
//
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Courier");
        //字体间隔

        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE,"8");
        //⼲扰线颜⾊
//
        properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, "white");
        //⼲扰实现类

        properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        //图⽚样式

        properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        //⽂字来源

        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "0123456789");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
