package com.gosenor.enums;

/**
 * @program: gosenor-healthy-mall
 * @description: 优惠卷枚举
 * @author: hcf
 * @create: 2021-08-02 13:58
 */
public class CouponEnum {
    /**
     * 优惠卷类型[NEW_USER注册赠券，TASK任务卷，PROMOTION
     * 促销劵]
     */
    public enum CouponCategoryEnum{
        NEW_USER("NEW_USER","注册赠券"),
        TASK("TASK","任务卷"),
        PROMOTION("PROMOTION","促销劵");

        private String category;
        private String name;

        private CouponCategoryEnum(String category,String name){
            this.category = category;
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 发布状态, PUBLISH发布，DRAFT草稿，OFFLINE下线
     */
    public enum CouponPublishEnum{
        PUBLISH,
        DRAFT,
        OFFLINE
    }

    public enum CouponStateEnum{
        NEW,
        USED,
        EXPIRED;
    }
}
