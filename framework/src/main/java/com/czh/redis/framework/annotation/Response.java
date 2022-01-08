package com.czh.redis.framework.annotation;

import java.lang.annotation.*;

/**
 * 标记是否需要经过统一返回, 用于区分我们的接口返回数据和spring本身的异常返回
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Response {
}
