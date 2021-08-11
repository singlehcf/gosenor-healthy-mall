package com.gosenor.service;

import com.gosenor.enums.SendCodeEnum;
import com.gosenor.utils.JsonData;

public interface NotifyService {
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to);

    /**
     * 验证验证码
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);
}
