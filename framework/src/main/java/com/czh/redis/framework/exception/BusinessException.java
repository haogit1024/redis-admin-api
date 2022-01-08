package com.czh.redis.framework.exception;

import com.czh.redis.common.emums.ResultEnum;
import lombok.Getter;

/**
 * @author czh
 * @date 2020/6/10
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ResultEnum resultEnum;

    public BusinessException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
