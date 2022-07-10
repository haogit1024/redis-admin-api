package com.czh.redis.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czh.redis.common.entity.FileLoadPath;
import com.czh.redis.common.mapper.FileLoadPathMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("local")
class FileLoadPathServiceTest {
    @Resource
    private FileLoadPathService service;

    @Test
    public void insertMockData() {
        String path = System.getProperty("user.home") + File.separator + "work_space";
        FileLoadPath fileLoadPath = new FileLoadPath();
        fileLoadPath.setPath(path);
        fileLoadPath.setCreator(1);
        fileLoadPath.setCreatedTime(LocalDateTime.now());
        fileLoadPath.setUpdater(1);
        fileLoadPath.setUpdatedTime(LocalDateTime.now());
        service.save(fileLoadPath);
    }

    @Test
    public void cachePathFile() {
        FileLoadPath fileLoadPath = service.getById(2);
        service.cachePathFile(fileLoadPath);
    }
}