package com.czh.redis.framework.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czh.redis.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author chenzh
 * @date 2020/12/3
 */
@Validated
@Transactional
public class BaseService<M extends BaseMapper<E>, E> extends ServiceImpl<M, E> {
    @Autowired
    protected RedisUtil redisUtil;
}
