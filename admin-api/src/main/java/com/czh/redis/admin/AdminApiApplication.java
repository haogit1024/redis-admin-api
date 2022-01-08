package com.czh.redis.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author czh
 * @date 2020/6/9
 */
@SpringBootApplication(scanBasePackages = "com.czh.web")
@MapperScan("com.czh.web.common.mapper")
public class AdminApiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApiApplication.class);
    }
}
