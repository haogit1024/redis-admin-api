package com.czh.redis.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author czh
 * 在不可扫描区域，不需要注入，作为一个普通的util类
 **/
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    public static String getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    public static Map getBeans(Class clazz) {
        return getApplicationContext().getBeansOfType(clazz);
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes() == null ? null : getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes() == null ? null : getRequestAttributes().getResponse();
    }

    private static ServletRequestAttributes getRequestAttributes() {

        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static Object getRequestAttribute(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getAttribute(name);
    }

    public static <T> T getRequestAttribute(String name, Class<T> clazz) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Object val = request.getAttribute(name);
        return clazz.cast(val);
    }

    /**
     * 从请求中获取userId
     * @return
     */
    public static Integer getJwtUserId() {
        if (getRequest() == null) {
            return null;
        }
        Object obj = getRequest().getAttribute("userId");
        if (obj == null) {
            return null;
        }
        return Integer.parseInt(obj.toString());
    }

    /**
     * 从请求中获取userName
     * @return
     */
    public static String getUserName() {
        if (getRequest() == null) {
            return null;
        }
        Object obj = getRequest().getAttribute("userName");
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getRequestIp() {
        if (getRequest() == null) {
            return null;
        }
        return getRequest().getRemoteAddr();
    }
}