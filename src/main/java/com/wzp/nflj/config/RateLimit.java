package com.wzp.nflj.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用时间滑动窗口限流
 *
 * @author zp.wei
 * @date 2023/6/14 13:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    int limit() default 5; //最大请求数

    int time() default 1; // 单位（秒）


}
