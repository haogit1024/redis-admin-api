package com.czh.redis.admin.service;

import com.czh.redis.admin.config.SysConfig;
import com.czh.redis.admin.view.RedisValueView;
import com.czh.redis.common.constants.CommonConstants;
import com.czh.redis.common.emums.ResultEnum;
import com.czh.redis.common.entity.Redis;
import com.czh.redis.common.mapper.RedisMapper;
import com.czh.redis.common.util.RSAUtil;
import com.czh.redis.common.util.SpringUtil;
import com.czh.redis.common.util.Utils;
import com.czh.redis.common.view.PageView;
import com.czh.redis.common.view.RedisView;
import com.czh.redis.framework.exception.BusinessException;
import com.czh.redis.framework.service.BaseCurdService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.czh.redis.common.emums.CommonEnum.BooleanType.TRUE;

@Service
@Slf4j
public class RedisService extends BaseCurdService<RedisMapper, Redis, RedisView> {
    @Autowired
    SysConfig sysConfig;

    /**
     * 测试是否可以连接
     * @param view
     * @return
     */
    public Integer test(@Valid RedisView view) {
        Jedis jedis = new Jedis(view.getHost(), Integer.parseInt(view.getPort()));
        try {
            if (StringUtils.isNotBlank(view.getPassword())) {
                jedis.auth(view.getPassword());
            }
            jedis.configGet("databases");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.REDIS_CONNECTION_ERROR.format(e.getMessage()));
        }
        return TRUE.getValue();
    }

    @Override
    public RedisView get(Integer id) {
        RedisView redisView = getRedisView(id);
        Jedis jedis = getJedis(id);
        // get databases
        int databases = 0;
        List<String> databaseList = jedis.configGet("databases");
        if (databaseList.size() == 2) {
            databases = Integer.parseInt(databaseList.get(1));
        }
        redisView.setDatabases(databases);
        List<Integer> databasesSize = new ArrayList<>(databases);
        for (int i = 0; i < databases; i++) {
            jedis.select(i);
            databasesSize.add(jedis.dbSize().intValue());
        }
        redisView.setDatabasesSize(databasesSize);
        redisView.setPassword(null);
        redisView.setLastConnectionTime(LocalDateTime.now());
        redisView.setLastConnectionIp(SpringUtil.getRequestIp());
        update(redisView);
        return redisView;
    }

    public RedisView getRedisView(Integer id) {
        String key = CommonConstants.RedisKey.REDIS_REDIS + id;
        if (!redisUtil.hasKey(key)) {
            Redis redis = getById(id);
            if (redis == null) {
                throw new BusinessException(ResultEnum.DATA_NOT_FOUND);
            }
            redisUtil.set(key, redis, 2L, TimeUnit.HOURS);
        }
        Redis redis = (Redis) redisUtil.get(key);
        return new RedisView(redis, sysConfig.getPrivateKey());
    }

    public Jedis getJedis(@NotNull Integer id) {
        RedisView redisView = getRedisView(id);
        try {
            return getJedis(redisView);
        } catch (Exception e) {
            log.error("id: {}, host: {}, port: {}, password: {}", redisView.getId(),
                    redisView.getHost(), redisView.getPort(), redisView.getPassword(), e);
            throw new BusinessException(ResultEnum.REDIS_CONNECTION_ERROR.format(e.getMessage()));
        }
    }

    public Set<String> listKeys(Integer id, Integer db) {
        Jedis jedis = this.getJedis(id);
        jedis.select(db);
        return jedis.keys("*");
    }

    @Override
    public Integer insert(RedisView vo) {
        return this.saveOrUpdate(vo);
    }

    @Override
    public Integer update(RedisView vo) {
        return this.saveOrUpdate(vo);
    }

    private Integer saveOrUpdate(RedisView vo) {
        Redis redis = new Redis();
        Utils.copyPropertiesIgnoreNull(vo, redis);
        if (StringUtils.isNotBlank(redis.getHost())) {
            redis.setHost(RSAUtil.encryptByPubKey(redis.getHost(), sysConfig.getPublicKey()));
        }
        if (StringUtils.isNotBlank(redis.getPort())) {
            redis.setPort(RSAUtil.encryptByPubKey(redis.getPort(), sysConfig.getPublicKey()));
        }
        if (StringUtils.isNotBlank(redis.getPassword())) {
            redis.setPassword(RSAUtil.encryptByPubKey(redis.getPassword(), sysConfig.getPublicKey()));
        }
        if (!saveOrUpdate(redis)) {
            throw new BusinessException(ResultEnum.DATA_SAVE_ERROR.format("redis"));
        }
        String key = CommonConstants.RedisKey.REDIS_REDIS + redis.getId();
        redisUtil.del(key);
        return redis.getId();
    }

    @Override
    public PageView<?> list(RedisView vo) {
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        int pageSize = vo.getPageSize() == null ? 10 : vo.getPageSize();
        Page<Redis> page = PageHelper.startPage(pageNum, pageSize);
        List<Redis> redisList = list(listQueryWrapper(vo));
        /*List<RedisView> viewList = new ArrayList<>(redisList.size());
        redisList.forEach(redis -> {
            RedisView redisView = new RedisView();
            Utils.copyPropertiesIgnoreNull(redis, redisView);
            viewList.add(redisView);
        });*/
        PageView<Redis> view = new PageView<>();
        view.setTotal(page.getTotal());
        view.setList(redisList);
        return view;
    }

    public Set<String> listKey(Integer id, Integer db) {
        Jedis jedis = getJedis(id, db);
        return jedis.keys("*");
    }

    public RedisValueView getValue(Integer id, Integer db, String key) {
        Jedis jedis = getJedis(id, db);
        String type = jedis.type(key);
        long ttl = jedis.ttl(key);
        log.info("type: {}", type);
        Object value = null;
        Long len = null;
        switch (type) {
            case "string": value = jedis.get(key); break;
            case "list":
                len = jedis.llen(key);
                value = jedis.lrange(key, 0, len);
                break;
            case "set":
                len = jedis.scard(key);
                value = jedis.smembers(key);
                break;
            case "zset":
                len = jedis.zcard(key);
                value = jedis.zrangeWithScores(key, 0, len);
                break;
            case "hash":
                len = jedis.hlen(key);
                value = jedis.hgetAll(key);
                break;
            default: throw new BusinessException(ResultEnum.TYPE_NOT_SUPPER.format(type));
        }
        return new RedisValueView(key, ttl, value, type, len);
    }

    public Integer setValue(Integer id, Integer db, String key, String value) {
        // TODO 支持5中数据类型的设置，目前只支持单个key的string类型设置
        Jedis jedis = getJedis(id, db);
        jedis.set(key, value);
        return TRUE.getValue();
    }

    public Integer deleteValue(Integer id, Integer db, String key) {
        Jedis jedis = getJedis(id, db);
        jedis.del(key);
        return TRUE.getValue();
    }

    public Jedis getJedis(RedisView view) {
        Jedis jedis = new Jedis(view.getHost(), Integer.parseInt(view.getPort()));
        if (StringUtils.isNotBlank(view.getPassword())) {
            jedis.auth(view.getPassword());
        }
        return jedis;
    }

    private Jedis getJedis(Integer id, Integer db) {
        RedisView view = getRedisView(id);
        Jedis jedis = getJedis(view);
        jedis.select(db);
        return jedis;
    }
}
