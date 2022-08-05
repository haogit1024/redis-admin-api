package com.czh.redis.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Profile({"prod", "test"})
//@Profile({"prod", "test", "local"})
@EnableScheduling
@Configuration
@Slf4j
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        taskRegistrar.setScheduler(executor);
        log.info("设置定时任务线程池");
    }
}
