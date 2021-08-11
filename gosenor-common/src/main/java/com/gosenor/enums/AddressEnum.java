package com.gosenor.enums;

import lombok.Data;

/**
 * @program: gosenor-healthy-mall
 * @description: 地址枚举类
 * @author: hcf
 * @create: 2021-07-29 10:25
 */
public class AddressEnum {

    /**
     * 默认地址状态
     */
    public enum DefaultStatus{
        DEFAULT_STATUS_0(0,"否"),
        DEFAULT_STATUS_1(1,"是");

        private int status;
        private String name;

        private DefaultStatus(int status,String name){
            this.status = status;
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
