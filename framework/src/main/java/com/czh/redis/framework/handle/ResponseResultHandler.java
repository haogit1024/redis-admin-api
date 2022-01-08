package com.czh.redis.framework.handle;

import com.alibaba.fastjson.JSON;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.result.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author czh
 * @date 2020/6/10
 **/
@ControllerAdvice
@Slf4j
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> controllerClass = returnType.getDeclaringClass();
        return controllerClass.isAnnotationPresent(Response.class) || returnType.hasMethodAnnotation(Response.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        log.info("selectedConverterType: {}", selectedConverterType.toString());
        log.info("response body: {}", JSON.toJSONString(body));
        if (body instanceof PlatformResult) {
            return body;
        }
        return PlatformResult.success(body);
    }

}
