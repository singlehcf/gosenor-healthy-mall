package com.gosenor.utils;

import com.gosenor.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: gosenor-healthy-mall
 * @description: 接⼝统⼀协议⼯具类
 * @author: hcf
 * @create: 2021-07-21 10:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonData<T> {
    /**
     * 状态码 200 表示成功，1表示处理中，-1表示失败
     */
    private Integer code;
    /**
     * 数据
     */
    private T data;
    /**
     * 描述
     */
    private String msg;
    /**
     * 成功，传⼊数据
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(200, null, null);
    }
    /**
     * 成功，传⼊数据
     * @param data
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(200, data, null);
    }
    /**
     * 失败，传⼊描述信息
     * @param msg
     * @return
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    /**
     * ⾃定义状态码和错误信息
     * @param code
     * @param msg
     * @return
     */
    public static JsonData buildCodeAndMsg(int code, String msg) {
        return new JsonData(code, null, msg);
    }
    /**
     * 传⼊枚举，返回信息
     * @param codeEnum
     * @return
     */
    public static JsonData buildResult(BizCodeEnum codeEnum){
        return JsonData.buildCodeAndMsg(codeEnum.getCode(),codeEnum.getMsg());
    }

    public boolean isSuccess(){
        if (this.code == 200){
            return true;
        }
        return false;
    }
}
