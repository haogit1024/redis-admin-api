package com.czh.redis.admin.interceptor;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.util.JwtUtil;
import com.czh.redis.framework.result.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class RequestParameterInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 打印请求信息
        String method = request.getMethod();
        log.info("http method: {}", method);
        if (!method.equals(HttpMethod.OPTIONS.name())) {
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
        // token校验
        if (!method.equals(HttpMethod.OPTIONS.name())) {
            String token = request.getHeader(jwtUtil.getHeaderName());
            if (!jwtUtil.verifyJwtAndRefresh(token)) {
                // 用 response 响应错误信息
                response.setStatus(HttpStatus.OK.value());
                response.setContentType(APPLICATION_JSON.toString());
                // 避免前端某些库报跨域错误
                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                PlatformResult result = PlatformResult.error(ResultEnum.TOKEN_ERROR);
                String responseContent = JSON.toJSONString(result);
                PrintWriter pw = response.getWriter();
                pw.write(responseContent);
                pw.flush();
                pw.close();
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
