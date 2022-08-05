package com.czh.redis.admin.schedule;

import com.czh.redis.admin.service.FileLoadPathService;
import com.czh.redis.common.entity.FileLoadPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * admin 模块定时任务, 添加事务处理
 * @author czh
 * @date 2020/6/10
 */
@Component
@Configurable
//@EnableScheduling
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class Schedule {
    @Resource
    private FileLoadPathService fileLoadPathService;

    /**
     * 固定10分钟刷新一次文件
     */
    @Scheduled(fixedDelay = 60 * 10 * 1000)
    public void refreshPath() {
        log.info("刷新文件定时任务开始执行");
        List<FileLoadPath> pathList = fileLoadPathService.list();
        pathList.forEach(fileLoadPathService::cachePathFile);
    }
}
