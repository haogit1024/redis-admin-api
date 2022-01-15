package com.czh.redis.admin.service;

import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.RedisView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class RedisServiceTest {
    @Autowired
    private RedisService service;

    private RedisView getTestRedisView() {
        return RedisView.builder()
                .host("localhost")
                .port("6379")
                .password("")
                .name("本地测试链接")
                .build();
    }

    @Test
    void test1() {
        service.test(getTestRedisView());
    }

    @Test
    void get() {
        RedisView view = service.get(1);
        System.out.println(Utils.object2Json(view));
    }

    @Test
    void listKeys() {
        Set<String> keys = service.listKeys(1, 1);
        System.out.println(Utils.object2Json(keys));
    }

    @Test
    void insert() {
        Integer id = service.insert(getTestRedisView());
        RedisView redisView = service.get(id);
        assert redisView != null : "添加redis失败";
    }

    @Test
    void update() {
    }

    @Test
    void list() {
    }

    @Test
    void listKey() {
    }

    @Test
    void getValue() {
    }

    @Test
    void setValue(){
    }

    @Test
    void deleteValue(){
    }

    @Test
    void testGetJedis() {
    }

    @Test
    void getRedisInfo() {
        RedisView view  = service.getRedisView(1);
        System.out.println(Utils.object2Json(view));
    }
}