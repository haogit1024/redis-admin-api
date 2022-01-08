package com.czh.redis.admin.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * admin 模块定时任务, 添加事务处理
 * @author czh
 * @date 2020/6/10
 */
@Component
@Configurable
@EnableScheduling
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class Schedule {
}
