package com.czh.redis.admin.controller;

import com.czh.redis.admin.service.RedisService;
import com.czh.redis.admin.view.RedisValueView;
import com.czh.redis.common.entity.Redis;
import com.czh.redis.common.mapper.RedisMapper;
import com.czh.redis.common.view.RedisView;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.controller.BaseRestController;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author czh
 */
@Response
@RestController
@RequestMapping("redis")
public class RedisController extends BaseRestController<RedisService, RedisMapper, Redis, RedisView> {
    @PostMapping("test")
    public Integer test(@RequestBody RedisView view) {
        return getServiceInstance().test(view);
    }

    @GetMapping("keys/{id}/{db}")
    public Set<String> listKeys(@PathVariable Integer id, @PathVariable Integer db) {
        return getServiceInstance().listKeys(id, db);
    }

    @GetMapping("{id}/{db}")
    public RedisValueView getValue(@PathVariable Integer id, @PathVariable Integer db, String key) {
        return getServiceInstance().getValue(id, db, key);
    }

    @PutMapping("{id}/{db}")
    public Integer setValue(@PathVariable Integer id, @PathVariable Integer db, String key, String value) {
        return getServiceInstance().setValue(id, db, key, value);
    }

    @DeleteMapping("{id}/{db}")
    public Integer deleteValue(@PathVariable Integer id, @PathVariable Integer db, String key) {
        return getServiceInstance().deleteValue(id, db, key);
    }
}
