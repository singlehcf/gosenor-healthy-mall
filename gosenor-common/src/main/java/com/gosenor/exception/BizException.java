package com.gosenor.exception;

import com.gosenor.enums.BizCodeEnum;
import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description: 全局异常处理
 * @author: hcf
 * @create: 2021-07-21 10:44
 */
@Data
public class BizException extends RuntimeException {

    private Integer code;
    private String msg;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public BizException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMsg());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMsg();
    }
}
