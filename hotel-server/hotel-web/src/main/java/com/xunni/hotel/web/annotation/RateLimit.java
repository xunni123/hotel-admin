package com.xunni.hotel.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 时间窗口（秒）
     */
    int window() default 60;

    /**
     * 时间窗口内最大请求数
     */
    int maxCount() default 100;

    /**
     * 限流类型
     */
    LimitType type() default LimitType.IP;

    /**
     * 限流类型枚举
     */
    enum LimitType {
        /**
         * 按IP限流
         */
        IP,
        /**
         * 按用户限流
         */
        USER,
        /**
         * 按接口限流
         */
        API
    }
}