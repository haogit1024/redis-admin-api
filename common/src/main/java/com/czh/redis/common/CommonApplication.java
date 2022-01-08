package com.czh.redis.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 只用来单元测试, 线上无实际用处
 * @author czh
 * @date 2020/6/15
 */
@SpringBootApplication(scanBasePackages = "com.czh.web.common")
@MapperScan("com.czh.web.common.mapper")
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}
