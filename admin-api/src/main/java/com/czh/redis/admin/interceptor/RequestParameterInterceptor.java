package com.czh.redis.admin.interceptor;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
public class RequestParameterInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        String method = request.getMethod();
        log.info("http method: {}", method);
        if (!method.equals("option")) {
            String contentType = request.getContentType();
            String body;
            if (StringUtils.isNotBlank(contentType) && contentType.contains("form")) {
                // 表单请求内容
                StringBuilder bodyBuilder = new StringBuilder();
                Enumeration<String> keys = request.getParameterNames();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    String val = Arrays.toString(request.getParameterValues(key));
                    bodyBuilder.append(key).append(": ").append(val).append("\n");
                }
                body = bodyBuilder.toString();
            } else {
                // 请求体的字符内容
                body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
            }
            String requestInfoTemplate = "URL: %s\n URI: %s \n ContextPath: %s\n method: %s\n content-type: %s\n queryString: %s\n body: %s";
            log.info(String.format(
                    requestInfoTemplate,
                    request.getRequestURL(),
                    request.getRequestURI(),
                    request.getContextPath(),
                    method,
                    contentType,
                    request.getQueryString(),
                    body
            ));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
