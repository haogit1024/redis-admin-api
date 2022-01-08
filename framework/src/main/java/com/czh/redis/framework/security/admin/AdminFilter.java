package com.czh.redis.framework.security.admin;

import com.alibaba.fastjson.JSON;
import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.util.JwtUtil;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.framework.result.PlatformResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static com.czh.redis.common.constants.CommonConstants.Request.REQ_ADMIN_USER_DETAILS;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * admin 模块 拦截器
 * 需要在 admin 模块注册为 bean
 * @author czh
 * @date 2020/6/17
 */
@Slf4j
public class AdminFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AdminUserDetailsService adminUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("开始验证");
        String jwt = request.getHeader(jwtUtil.getHeaderName());
        if (jwt != null) {
            if (verifyJwt(request, jwt)) {
                // 设置认证
                AdminUserDetails userDetails = adminUserDetailsService.loadUserById(SpringUtil.getJwtUserId());
                if (userDetails != null) {
                    request.setAttribute(REQ_ADMIN_USER_DETAILS, userDetails);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
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
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean verifyJwt(HttpServletRequest request, String jwt) {
        try {
            if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
                return true;
            }
            return jwtUtil.verifyJwtAndRefresh(jwt);
        } catch (Exception e) {
            // 备用方案 防止报错
            return false;
        }
    }
}
