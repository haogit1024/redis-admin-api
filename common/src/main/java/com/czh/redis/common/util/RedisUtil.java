package com.czh.redis.common.util;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author czh
 **/
@Component
@Data
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 备用方案, 如果有需要其他模块可以使用这个方法设置不同的redis配置
     * @param redisTemplate
     */
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setEnableTransactionSupport(Boolean enableTransactionSupport) {
        this.redisTemplate.setEnableTransactionSupport(enableTransactionSupport);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        return expireOfTimeUnit(key, time, TimeUnit.SECONDS);
    }


    /**
     * 指定缓存失效时间
     *
     * @param key      键
     * @param time     时间(自定义)
     * @param timeUnit 时间单位
     * @return
     */
    public boolean expireOfTimeUnit(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key  键 不能为null
     * @param unit 时间单位
     * @return 返回0代表为永久有效
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量获取keys
     *
     * @param patten 键
     * @return 批量获取keys
     */
    public Set<String> keys(String patten) {
        try {
            return redisTemplate.keys(patten);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取key的类型
     *
     * @param key
     * @return
     */
    public DataType getType(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 批量删除缓存
     *
     * @param keys 多个
     */
    @SuppressWarnings("unchecked")
    public void del(Set<String> keys) {
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取keys对应的所有键值
     *
     * @param keys 键
     * @return 对应的多个键值
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        return set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间(时间单位) time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit 时间单位
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * 分页get with scores
     *
     * @param key    redis key
     * @param cursor 下标
     * @param after  行数，当值为正数时，按score倒序获取;负数时，按score正序获取
     * @return
     */
    /*public Set<ZSetOperations.TypedTuple<Object>> hPageGetWithScores(String key, Double cursor, Integer after) {
        Set<ZSetOperations.TypedTuple<Object>> returnData;
        // 从缓存中取得分页数量
        // 假如是加载最新数据就不用分数判断，直接取得最后n条
        if (cursor.longValue() == CommonConstants.Public.LIST_LAST_CURSOR) {
            if (after < 0) {
                returnData = getZSetRangeWithScores(key, 0, Math.abs(after) - 1);
            } else {
                returnData = getZSetReverseRangeWithScores(key, 0, after - 1);
            }
        } else {
            if (after < 0) {
                returnData = getZSetRangeByScoreWithScores(key, cursor, Double.MAX_VALUE, 1, Math.abs(after));
            } else {
                returnData = getZSetReverseRangeByScoreWithScores(key, 0, cursor, 1, after);
            }
        }
        return returnData;
    }*/

    /**
     * 分页get
     *
     * @param key    redis key
     * @param cursor 下标
     * @param after  行数，当值为正数时，按score倒序获取;负数时，按score正序获取
     * @return
     */
    /*public Set<Object> hPageGet(String key, Double cursor, Integer after) {
        Set<Object> returnData;
        // 从缓存中取得分页数量
        // 假如是加载最新数据就不用分数判断，直接取得最后n条
        if (cursor.longValue() == CommonConstants.Public.LIST_LAST_CURSOR) {
            if (after < 0) {
                returnData = getZSetRange(key, 0, Math.abs(after) - 1);
            } else {
                returnData = getZSetReverseRange(key, 0, after - 1);
            }
        } else {
            if (after < 0) {
                returnData = getZSetRangeByScore(key, cursor, Double.MAX_VALUE, 1, Math.abs(after));
            } else {
                returnData = getZSetReverseRangeByScore(key, 0, cursor, 1, after);
            }
        }
        return returnData;
    }*/

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public List<Object> multiGet(String key, Collection<Object> items) {
        return redisTemplate.opsForHash().multiGet(key, items);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<Object, Object> map) {
        return hmset(key, map, 0, TimeUnit.SECONDS);

    }

    /**
     * 获得Hash的大小
     *
     * @param key 键
     */
    public long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<Object, Object> map, long time) {
        return hmset(key, map, time, TimeUnit.SECONDS);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key      键
     * @param map      对应多个键值
     * @param time     时间(秒)
     * @param timeUnit 时间单位
     * @return
     */
    public boolean hmset(String key, Map<Object, Object> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expireOfTimeUnit(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        return hset(key, item, value, 0, TimeUnit.SECONDS);

    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        return hset(key, item, value, time, TimeUnit.SECONDS);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expireOfTimeUnit(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * list right pop
     */
    public Object lRightPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //===============================ZSet=================================

    /**
     * 通过分数删除ZSet中的值
     *
     * @param key
     * @param min
     * @param max
     */
    public void removeZSetRangeByScore(String key, double min, double max) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 移除key 对应的value
     *
     * @param key
     * @param value
     * @return
     */
    public Long removeZSetValue(String key, Object... value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 删除，键为K的集合，索引start<=index<=end的元素子集
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public void removeZSetRange(String key, Long start, Long end) {
        redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 并集 将key对应的集合和key1对应的集合合并到key2中
     * 如果分数相同的值，都会保留
     * 原来key2的值会被覆盖
     *
     * @param key
     * @param key1
     * @param key2
     */
    public void setZSetUnionAndStore(String key, String key1, String key2) {
        redisTemplate.opsForZSet().unionAndStore(key, key1, key2);
    }

    /**
     * 获取整个有序集合ZSET，正序
     *
     * @param key
     */
    public Set<Object> getZSetRange(String key) {
        return getZSetRange(key, 0, -1);
    }

    /**
     * 获取有序集合ZSET
     * 键为K的集合，索引start<=index<=end的元素子集，正序
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key
     * @param start 开始位置
     * @param end   结束位置
     */
    public Set<Object> getZSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取整个有序集合ZSET，倒序
     *
     * @param key
     */
    public Set<Object> getZSetReverseRange(String key) {
        return getZSetReverseRange(key, 0, -1);
    }

    public Set<Object> getZSetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set<Object> getZSetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }


    /**
     * 通过分数(权值)获取ZSET集合 正序 -从小到大
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> getZSetRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 通过分数(权值)获取ZSET集合 正序 -从小到大 (含分值）
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 通过分数(权值)获取ZSET集合 倒序 -从大到小
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> getZSetReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 通过分数(权值)获取ZSET集合 倒序 -从大到小
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> getZSetReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * 通过分数(权值)获取ZSET集合 倒序 -从大到小(含分值）
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    /**
     * 键为K的集合，索引start<=index<=end的元素子集
     * 返回泛型接口（包括score和value），正序
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 键为K的集合，索引start<=index<=end的元素子集
     * 返回泛型接口（包括score和value），倒序
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 键为K的集合
     * 返回泛型接口（包括score和value），正序
     *
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetRangeWithScores(String key) {
        return getZSetRangeWithScores(key, 0, -1);
    }

    /**
     * 键为K的集合
     * 返回泛型接口（包括score和value），倒序
     *
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getZSetReverseRangeWithScores(String key) {
        return getZSetReverseRangeWithScores(key, 0, -1);
    }

    /**
     * 键为K的集合，sMin<=score<=sMax的元素个数
     *
     * @param key
     * @param sMin
     * @param sMax
     * @return
     */
    public long getZSetCountSize(String key, double sMin, double sMax) {
        return redisTemplate.opsForZSet().count(key, sMin, sMax);
    }

    /**
     * 获取Zset 键为K的集合元素个数
     *
     * @param key
     * @return
     */
    public Long getZSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取键为K的集合，value为obj的元素分数
     *
     * @param key
     * @param value
     * @return
     */
    public Double getZSetScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 元素分数增加，delta是增量
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    public Double incrementZSetScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 向有序集合ZSET 里面添加元素
     * 默认按照score升序排列，存储格式K(1)==Object(n)，Object(1)=S(1)
     *
     * @param key
     * @param score
     * @param value
     * @return
     */
    public Boolean addZSet(String key, double score, Object value) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 添加有序集合ZSET
     *
     * @param key
     * @param value
     * @return
     */
    public Long addZSet(String key, Set<ZSetOperations.TypedTuple<Object>> value) {
        return redisTemplate.opsForZSet().add(key, value);
    }

    /**
     * 批量向有序集合ZSET 里面添加元素
     *
     * @param key
     * @param score
     * @param value
     * @return
     */

    public Boolean addZSet(String key, double[] score, Object[] value) {
        if (score.length != value.length) {
            return false;
        }
        for (Integer i = 0; i < score.length; i++) {
            if (addZSet(key, score[i], value[i]) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重命名redisKey
     */
    public boolean rename(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
            redisTemplate.expire(newKey, 1, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回一个sorted set的元素总个数
     *
     * @param key 缓存的key
     * @return 元素总个数
     */
    public long countZSet(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}