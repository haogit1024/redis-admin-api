package com.czh.redis.framework.handle;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.czh.redis.framework.exception.BusinessException;
import com.czh.redis.framework.result.PlatformResult;
import com.czh.redis.common.emums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author czh
 * @date 2020/6/11
 */
@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理通用自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public PlatformResult handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("handleBusinessException start, uri:{}, exception:{}, caused by: {}"
                , request.getRequestURI()
                , e.getClass()
                , e.getMessage());

        return PlatformResult.error(e.getResultEnum());
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return 响应自定义信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public PlatformResult handleBindGetException(ConstraintViolationException e) {
        List<String> defaultMsg = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        String errMsg = e.getMessage();
        if (CollectionUtils.isNotEmpty(defaultMsg)) {
            errMsg = StringUtils.join(defaultMsg, ",");
        }
        return PlatformResult.error(ResultEnum.PARAM_ERROR.format(errMsg));
    }

    /**
     * 处理运行时系统异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public PlatformResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        // 这里可以发送邮件给开发的小伙伴
        log.error("handleRuntimeException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return PlatformResult.error(ResultEnum.SERVER_ERROR);
    }
}
