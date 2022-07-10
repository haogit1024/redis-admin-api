package com.czh.redis.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.czh.redis.framework.handle.DatabaseMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new DatabaseMetaObjectHandler();
    }
}
