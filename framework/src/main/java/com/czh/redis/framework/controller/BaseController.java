package com.czh.redis.framework.controller;

import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.service.BaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.ParameterizedType;

/**
 * @author chenzh
 * @date 2020/12/3
 */
@Validated
@Transactional(rollbackFor = Exception.class)
@Response
public class BaseController<S extends BaseService> {
    public S getServiceInstance() {
        try {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            // 通过反射获取泛型的真实类型(class)
            Class<S> clazz = (Class<S>) pt.getActualTypeArguments()[0];
            // 通过class从springApplicationContext中获取已创建的bean
            return SpringUtil.getBean(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}