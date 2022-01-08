package com.czh.redis.admin.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.czh.redis.admin.interceptor.RequestParameterInterceptor;
import com.czh.redis.framework.wrapper.RequestWrapperFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNonStringKeyAsString;

/**
 * @author czh
 * @date 2020/6/10
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                PrettyFormat,
                WriteNonStringKeyAsString
        );
        // TODO localDateTime转时间戳
        fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConvert.setSupportedMediaTypes(fastMediaTypes);
        fastConvert.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConvert);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 域名全部通過
                .allowedOrigins("*")
                .allowCredentials(true)
                // 所有请求方法通過
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Bean
    public RequestParameterInterceptor requestParameterInterceptor() {
        return new RequestParameterInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestParameterInterceptor()).addPathPatterns("/**");
    }

    /**
     * 注册请求参数拦截器，因为要设置过滤顺序所以不能用 @WebFilter 注解注册给拦截器
     *
     * @return
     */
    @Bean
    @Profile({"dev", "test", "prod", "local"})
    public FilterRegistrationBean<RequestWrapperFilter> thirdFilter() {
        FilterRegistrationBean<RequestWrapperFilter> bean = new FilterRegistrationBean<>(new RequestWrapperFilter());
        bean.setName("requestWrapperFilter");
        bean.addUrlPatterns("/*");
        // 必须在 拦截器之前执行
        bean.setOrder(0);
        return bean;
    }
}
