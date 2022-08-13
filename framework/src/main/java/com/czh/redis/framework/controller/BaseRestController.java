package com.czh.redis.framework.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czh.redis.common.view.PageView;
import com.czh.redis.framework.annotation.Response;
import com.czh.redis.framework.service.BaseCurdService;
import com.czh.redis.common.entity.BaseEntity;
import com.czh.redis.common.view.BaseView;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author czh
 * @date 2020/6/10
 */
@Response
public class BaseRestController<S extends BaseCurdService<M, E, V>, M extends BaseMapper<E>, E extends BaseEntity, V extends BaseView> extends BaseController<S> {

    @GetMapping("/{id}")
    public Object get(@PathVariable Integer id) {
        return getServiceInstance().get(id);
    }

    @GetMapping
    public PageView<?> list(@RequestParam Map<String, Object> map) {
        return getServiceInstance().list(map);
    }

    @PostMapping
    public Integer insert(@RequestBody V vo) {
        return getServiceInstance().insert(vo);
    }

    @PutMapping
    public Integer update(@RequestBody V vo) {
        return getServiceInstance().update(vo);
    }

    @DeleteMapping("/{id}")
    public Integer delete(@PathVariable Integer id) {
        return getServiceInstance().delete(id);
    }
}
