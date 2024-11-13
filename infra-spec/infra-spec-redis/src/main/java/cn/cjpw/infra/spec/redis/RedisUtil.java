package cn.cjpw.infra.spec.redis;

import cn.cjpw.infra.spec.base.exception.builder.ExBuilder;
import cn.cjpw.infra.spec.base.exception.enums.IError;
import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
import cn.cjpw.infra.spec.redis.redisson.MultiRedissonLockBuilder;
import cn.cjpw.infra.spec.redis.redisson.SingleRedissonLockBuilder;
import cn.cjpw.infra.spec.redis.template.JsonRedisTemplate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.data.redis.domain.geo.Metrics;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * redis 缓存工具,简化redisTemplate操作
 * <p>
 * 基于spring和redis的redisTemplate工具类
 * <p>
 * 针对所有的Map 都是以h开头的方法
 * <p>
 * 针对所有的Set 都是以s开头的方法
 * <p>
 * 针对所有的List 都是以l开头的方法
 * <p>
 * 针对所有的sortSet 都是以zs开头的方法
 * <p>
 * 针对所有的Geo 都是以g开头的
 *
 * @author: chenjun
 */
public class RedisUtil {

    private JsonRedisTemplate redisTemplate;
    private RedissonLockWrapper lockSupporter;

    public RedisUtil(JsonRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedissonLockSupporter(RedissonLockWrapper lockSupporter) {
        this.lockSupporter = lockSupporter;
    }

    /**
     * key分隔符
     */
    public static final String DELIMITER = ":";

    /* ******************************************************** cacheExtend start ************************************************************/

    /**
     * 获取一个缓存优先的策略工具 用于防止缓存击穿、穿透、雪崩等场景
     *
     * @return
     */
    public PriorityCache getPriorityCache() {
        return new PriorityCache(this);
    }

    /* ******************************************************** cacheExtend end ************************************************************/

    /* ******************************************************** lock start ************************************************************/

    /**
     * 获取分布式锁 默认一直等待，直到获取锁
     *
     * @param lockName
     * @return
     */
    public SingleRedissonLockBuilder getLock(String lockName) {
        return lockSupporter.getLock(lockName);
    }

    //    /**
    //     * 获取分布式锁 默认一直等待，直到获取锁
    //     *
    //     * @param lockName
    //     * @return
    //     */
    //    public SingleRedissonLockBuilder getReadLock(String lockName) {
    //        return lockSupporter.getReadLock(lockName);
    //    }
    //
    //    /**
    //     * 获取分布式锁 默认一直等待，直到获取锁
    //     *
    //     * @param lockName
    //     * @return
    //     */
    //    public SingleRedissonLockBuilder getWriteLock(String lockName) {
    //        return lockSupporter.getWriteLock(lockName);
    //    }

    /**
     * 获取分布式锁 快速定义等待策略
     *
     * @param lockName
     * @return
     */
    public SingleRedissonLockBuilder getLock(String lockName, LockExpire lockExpire) {
        return lockSupporter.getLock(lockName, lockExpire);
    }

    /**
     * 获取分布式锁 不等待，不关注锁获取失败相关异常
     *
     * @param lockName
     * @return
     */
    public SingleRedissonLockBuilder getLockNoWait(String lockName) {
        return lockSupporter.getLockNoWait(lockName);
    }

    /**
     * 获取分布式联锁 快速定义等待策略
     *
     * @param lockNames
     * @param lockExpire
     * @return
     */
    public MultiRedissonLockBuilder getMultiLock(List<String> lockNames, LockExpire lockExpire) {
        return lockSupporter.getMultiLock(lockNames, lockExpire);
    }

    /**
     * 获取分布式联锁 不等待，不关注锁获取失败相关异常
     *
     * @param lockNames
     * @return
     */
    public MultiRedissonLockBuilder getMultiLockNoWait(List<String> lockNames) {
        return lockSupporter.getMultiLockNoWait(lockNames);
    }

    /**
     * 查询任意线程正持有锁
     *
     * @param lockName
     * @return
     */
    public boolean hasLock(String lockName) {
        return lockSupporter.hasLock(lockName);
    }

    /**
     * 查询是否当前线程正持有锁
     *
     * @param lockName
     * @return
     */
    public boolean hasLockByCurrent(String lockName) {
        return lockSupporter.hasLockByCurrent(lockName);
    }

    /**
     * 查询指定线程是否正持有锁
     *
     * @param lockName
     * @return
     */
    public boolean hasLockByThread(String lockName, int threadId) {
        return lockSupporter.hasLockByThread(lockName, threadId);
    }

    /* ******************************************************** lock end ************************************************************/

    /* ******************************************************** common start ************************************************************/

    /**
     * 公共方法 指定KEY不失效
     *
     * @param key 键
     * @return
     */
    public boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 公共方法 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return true;
    }

    /**
     * 公共方法 指定缓存失效时间
     *
     * @param key      键
     * @param time
     * @param timeUnit
     * @return
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.expire(key, time, timeUnit);
        }
        return true;
    }

    /**
     * 公共方法 指定缓存失效时间
     *
     * @param key  键
     * @param date 过期时间
     * @return
     */
    public boolean expireAt(String key, Date date) {
        if (date != null) {
            redisTemplate.expireAt(key, date);
        }
        return true;
    }

    /**
     * 公共方法 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 公共方法 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 公共方法 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 公共方法 删除缓存
     *
     * @param keySet 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(Set<String> keySet) {
        redisTemplate.delete(keySet);
    }

    /**
     * 公共方法 原子递增 1
     *
     * @param key 键
     * @return
     */
    public long increaseOne(String key) {
        return increase(key, 1);
    }

    /**
     * 公共方法 原子递增
     *
     * @param key 键
     * @param by  递增值(正整数)
     * @return
     */
    public long increase(String key, long by) {
        if (by < 0) {
            ExBuilder.of(IError.VALIDATION_PARAM_FAIL).withShowMsg("递增因子必须正整数").buildAndThrow();
        }
        return redisTemplate.opsForValue().increment(key, by);
    }

    /**
     * 公共方法 原子递减 1
     *
     * @param key 键
     * @return
     */
    public long decreaseOne(String key) {
        return decrease(key, 1);
    }

    /**
     * 公共方法 原子递减
     *
     * @param key 键
     * @param by  递减值(正整数)
     * @return
     */
    public long decrease(String key, long by) {
        if (by < 0) {
            ExBuilder.of(IError.VALIDATION_PARAM_FAIL).withShowMsg("递减因子必须正整数").buildAndThrow();
        }
        return redisTemplate.opsForValue().decrement(key, by);
    }

    /**
     * 将序列化过的缓存值转为指定的java对象 无内容返回null
     *
     * @param val
     * @param z
     */
    public <T> T toJavaBean(Object val, Class<T> z) {
        return redisTemplate.toJavaBean(val, z);
    }

    /**
     * 获取指定类型list 无内容返回null
     *
     * @param val
     * @param z
     * @param <T>
     * @return
     */
    public <T> List<T> toJavaList(Object val, Class<T> z) {
        return redisTemplate.toJavaList(val, z);
    }

    /**
     * 获取指定类型set 无内容返回null
     *
     * @param val
     * @param z
     * @param <T>
     * @return
     */
    public <T> Set<T> toJavaSet(Object val, Class<T> z) {
        return redisTemplate.toJavaSet(val, z);
    }

    /**
     * 模糊匹配key
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        return this.redisTemplate.keys(key);
    }


    /**
     * 使用scan命令 模糊匹配key
     *
     * @param key
     * @return
     */
    public Set<String> scanKeys(String key) {
        return redisTemplate.scanKeys(key);
    }

    /**
     * 使用scan命令 模糊匹配key个数
     *
     * @param key
     * @return key个数
     */
    public long scanKeySize(String key) {
        return redisTemplate.scanKeySize(key);
    }

    /* ******************************************************** common end ************************************************************/

    /* ******************************************************** bucket start ************************************************************/

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
     * 普通缓存获取 如果value为基础类型 不需要调用此接口
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> z) {
        Object val = redisTemplate.opsForValue().get(key);
        return redisTemplate.toJavaBean(val, z);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        set(key, value, -1);
        return true;

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间 秒
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
     * @param time     时间 time要正整数 如果time小于等于0 将设置无限期
     * @param timeUnit
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
        return true;
    }

    /**
     * 没有时放入
     *
     * @param key   键
     * @param value 值
     * @return 返回旧值, 没有时返回null
     */
    public void setIfAbsent(String key, Object value) {
        setIfAbsent(key, value, -1);
    }

    /**
     * 没有时放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间 秒
     * @return 返回旧值, 没有时返回null
     */
    public void setIfAbsent(String key, Object value, long time) {
        setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 没有时放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间 time要正整数 如果time小于等于0 将设置无限期
     * @param timeUnit
     * @return 返回旧值, 没有时返回null
     */
    public void setIfAbsent(String key, Object value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().setIfAbsent(key, value);
        }
    }

    /**
     * 存在时放入
     *
     * @param key   键
     * @param value 值
     * @return 返回旧值, 没有时返回null
     */
    public Object setIfPresent(String key, Object value) {
        return setIfPresent(key, value, -1);
    }

    /**
     * 存在时放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间 秒
     * @return 返回旧值, 没有时返回null
     */
    public Object setIfPresent(String key, Object value, long time) {
        return setIfPresent(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 存在时放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间 time要正整数 如果time小于等于0 将设置无限期
     * @param timeUnit
     * @return 返回旧值, 没有时返回null
     */
    public Object setIfPresent(String key, Object value, long time, TimeUnit timeUnit) {
        Object object = redisTemplate.opsForValue().get(key);
        if (time > 0) {
            redisTemplate.opsForValue().setIfPresent(key, value, time, timeUnit);
        } else {
            redisTemplate.opsForValue().setIfPresent(key, value);
        }
        return object;
    }

    /**
     * 通过key批量获取value
     *
     * @param keySet
     * @return
     */
    public List multiGet(Set<String> keySet) {
        return redisTemplate.opsForValue().multiGet(keySet);
    }

    /**
     * 通过key批量获取value
     *
     * @param keySet
     * @return
     */
    public <T> List<T> multiGet(Set<String> keySet, Class<T> tClass) {
        return this.toJavaList(redisTemplate.opsForValue().multiGet(keySet), tClass);
    }

    /**
     * 批量设置
     *
     * @param map
     */
    public void multiSet(Map<String, Object> map) {
        if (CollectionUtil.isNotEmpty(map)) {
            redisTemplate.opsForValue().multiSet(map);
        }
    }

    /**
     * 批量设置
     *
     * @param map
     */
    public void multiSet(Map<String, Object> map, long time, TimeUnit timeUnit) {
        if (CollectionUtil.isNotEmpty(map)) {
            if (time > 0) {
                map.forEach((key, value) -> {
                    redisTemplate.opsForValue().set(key, value, time, timeUnit);
                });
            } else {
                redisTemplate.opsForValue().multiSet(map);
            }

        }
    }

    /**
     * 批量设置
     *
     * @param map
     */
    public void multiSetIfAbsent(Map<String, Object> map) {
        if (CollectionUtil.isNotEmpty(map)) {
            redisTemplate.opsForValue().multiSetIfAbsent(map);
        }

    }

    /**
     * 批量设置
     *
     * @param map
     */
    public void multiSetIfAbsent(Map<String, Object> map, long time, TimeUnit timeUnit) {
        if (CollectionUtil.isNotEmpty(map)) {
            if (time > 0) {
                map.forEach((key, value) -> {
                    redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
                });
            } else {
                redisTemplate.opsForValue().multiSetIfAbsent(map);
            }
        }
    }

    /* ******************************************************** bucket end ************************************************************/

    /* ******************************************************** map start ************************************************************/

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> T hGet(String key, String item, Class<T> z) {
        Object val = redisTemplate.opsForHash().get(key, item);
        return redisTemplate.toJavaBean(val, z);
    }

    /**
     * HashGet value需要使用{@link RedisUtil#toJavaBean(Object, Class)}
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public List<Object> hMultiGet(String key, Collection<String> item) {
        return redisTemplate.opsForHash().multiGet(key, item);
    }

    /**
     * HashGet 确保value都是同一种类型
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> List<T> hMultiGet(String key, Collection<String> item, Class<T> z) {
        List<Object> val = redisTemplate.opsForHash().multiGet(key, item);
        return redisTemplate.toJavaList(val, z);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value) {
        return hSet(key, item, value, 0);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间 秒 重置的是整个hashmap的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value, long time) {
        return hSet(key, item, value, time, TimeUnit.SECONDS);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key      键
     * @param item     项
     * @param value    值
     * @param time     时间  重置的是整个hashmap的时间
     * @param timeUnit
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String item, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                redisOperations.opsForHash().put(key, item, value);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSetIfAbsent(String key, String item, Object value) {
        return hSetIfAbsent(key, item, value, 0);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间 秒 重置的是整个hashmap的时间
     * @return true 成功 false失败
     */
    public boolean hSetIfAbsent(String key, String item, Object value, long time) {
        return hSetIfAbsent(key, item, value, time, TimeUnit.SECONDS);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key      键
     * @param item     项
     * @param value    值
     * @param time     时间  重置的是整个hashmap的时间
     * @param timeUnit
     * @return true 成功 false失败
     */
    public boolean hSetIfAbsent(String key, String item, Object value, long time, TimeUnit timeUnit) {
        long expire = time <= 0 ? -1L : time;
        boolean suc = redisTemplate.opsForHash().putIfAbsent(key, item, value);
        if (suc && expire > 0) {
            redisTemplate.expire(key, expire, timeUnit);
        }
        return true;
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, String item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, String[] item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, Collection<String> item) {
        redisTemplate.opsForHash().delete(key, item.toArray());
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
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, Object> hMapGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取整个map warning: 如果map中的value仍然为泛型，则value泛型获取到的结果并非期望
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hMapGet(String key, Class<T> tClass) {
        Map<String, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, T> outPutMap = new HashMap<>(map.size());
        map.forEach((k, v) -> {
            outPutMap.put(k, redisTemplate.toJavaBean(v, tClass));
        });
        return outPutMap;
    }

    /**
     * 获取整个map,直接转化为指定bean warning: 如果map中的value仍然为泛型，则value泛型获取到的结果并非期望
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T hMapGetBean(String key, Class<T> tClass) {
        Map<String, Object> map = redisTemplate.opsForHash().entries(key);
        return this.toJavaBean(map, tClass);
    }

    /**
     * HashSet
     *
     * @param key  键
     * @param bean
     * @return true 成功 false 失败
     */
    public boolean hMapSet(String key, Object bean) {
        return hMapSet(key, BeanUtil.beanToMap(bean));
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hMapSet(String key, Map<String, ?> map) {
        return hMapSet(key, map, 0);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hMapSet(String key, Map<String, ?> map, long time) {
        return hMapSet(key, map, time, TimeUnit.SECONDS);
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间
     * @return true成功 false失败
     */
    public boolean hMapSet(String key, Map<String, ?> map, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                Map<String, Object> mapInput = new HashMap<>(map.size());
                mapInput.putAll(map);
                redisOperations.opsForHash().putAll(key, mapInput);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }

    /**
     * hash递增1 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @return
     */
    public double hIncreaseOne(String key, String item) {
        return hIncrease(key, item, 1);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   递增值(正整数)
     * @return
     */
    public double hIncrease(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * hash递减1
     *
     * @param key  键
     * @param item 项
     * @return
     */
    public double hDecreaseOne(String key, String item) {
        return hDecrease(key, item, 1);
    }

    /**
     * hash递减1
     *
     * @param key  键
     * @param item 项
     * @param by   递减值(正整数)
     * @return
     */
    public double hDecrease(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /* ******************************************************** map end ************************************************************/
    /* ******************************************************** set start ************************************************************/

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> sGet(String key, Class<T> tClass) {
        return this.toJavaSet(this.sGet(key), tClass);
    }

    /**
     * 随机取出,取出的元素不会重复
     *
     * @param key
     * @param count 取出个数
     * @return
     */
    public Set<Object> sGetRandom(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 随机取出,取出的元素不会重复
     *
     * @param key
     * @param count 取出个数
     * @return
     */
    public <T> Set<T> sGetRandom(String key, long count, Class<T> tClass) {
        return toJavaSet(this.sGetRandom(key, count), tClass);
    }

    /**
     * 随机取出,并从集合中移除
     *
     * @param key
     * @param count 取出个数
     * @return
     */
    public List<Object> sGetPop(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    /**
     * 随机取出,并从集合中移除
     *
     * @param key
     * @param count 取出个数
     * @return
     */
    public <T> List<T> sGetPop(String key, long count, Class<T> tClass) {
        return toJavaList(this.sGetPop(key, count), tClass);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHas(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 将单个值放入set缓存
     *
     * @param key   键
     * @param value 值
     * @return 成功个数
     */
    public long sAdd(String key, Object value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值
     * @return 成功个数
     */
    public long sSet(String key, Collection<?> values) {
        return redisTemplate.opsForSet().add(key, values.toArray());
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Collection<?> values, long time) {
        return sSet(key, values, time, TimeUnit.SECONDS);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Collection<?> values, long time, TimeUnit timeUnit) {
        List<Object> list = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                redisOperations.opsForSet().add(key, values.toArray());
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return (Long) list.get(0);
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Set<?> values) {
        Long count = redisTemplate.opsForSet().remove(key, values.toArray());
        return count;
    }

    //    /**
    //     * 移除值为value的
    //     *
    //     * 一定要记得区分 集合类型和单个value的区别
    //     *
    //     * @param key    键
    //     * @param value 值
    //     * @return 移除的个数
    //     */
    //    public long sRemove(String key, Object value) {
    //        Long count = redisTemplate.opsForSet().remove(key, value);
    //        return count;
    //    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object[] values) {
        Long count = redisTemplate.opsForSet().remove(key, values);
        return count;
    }

    /* ******************************************************** set end ************************************************************/
    /* ******************************************************** sortSet start ************************************************************/

    /**
     * 根据key获取sort Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set zsGet(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * 根据key获取sort Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsGet(String key, Class<T> tClass) {
        return this.toJavaSet(this.zsGet(key), tClass);
    }

    /**
     * 根据key获取sort Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set zsGetWithScore(String key) {
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    /**
     * 根据key获取sort Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsGetWithScore(String key, Class<T> tClass) {
        return this.toJavaSet(this.zsGetWithScore(key), tClass);
    }

    /**
     * 根据value从一个sort set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean zsHas(String key, Object value) {
        Long index = redisTemplate.opsForZSet().rank(key, value);
        return index == null || index < 0 ? false : true;
    }

    /**
     * 获取排名 分值越低越靠前
     *
     * @param key   键
     * @param value 值
     * @return -1 不在排名中 >=0 base on 0
     */
    public long zsRank(String key, Object value) {
        Long index = redisTemplate.opsForZSet().rank(key, value);
        long rank = index == null || index < 0 ? -1 : index.longValue();
        return rank;
    }

    /**
     * 获取排名 分值越高越靠前
     *
     * @param key   键
     * @param value 值
     * @return -1 不在排名中 >=0 base on 0
     */
    public long zsReverseRank(String key, Object value) {
        Long index = redisTemplate.opsForZSet().reverseRank(key, value);
        long rank = index == null || index < 0 ? -1 : index.longValue();
        return rank;
    }

    /**
     * 修改分值
     *
     * @param key
     * @param value
     */
    public void zsIncrement(String key, ZSetOperations.TypedTuple value) {
        redisTemplate.opsForZSet().incrementScore(key, value.getValue(), value.getScore());
    }

    /**
     * 将数据放入sort set缓存
     *
     * @param key   键
     * @param value 值
     * @return 成功个数
     */
    public boolean zsSet(String key, ZSetOperations.TypedTuple value) {
        return zsSet(key, value, 0);
    }

    /**
     * 将sort set数据放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 成功个数
     */
    public boolean zsSet(String key, ZSetOperations.TypedTuple value, long time) {
        return zsSet(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 将sort set数据放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return 成功个数
     */
    public boolean zsSet(String key, ZSetOperations.TypedTuple value, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                redisOperations.opsForZSet().add(key, value.getValue(), value.getScore());
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }


    /**
     * 将数据放入sort set缓存
     *
     * @param key
     * @param values
     * @return 成功
     */
    public boolean zsSet(String key, Set<? extends ZSetOperations.TypedTuple> values) {
        return zsSet(key, values, 0);
    }

    /**
     * 将数据放入sort set缓存
     *
     * @param key
     * @param values
     * @param time   秒
     * @return 成功
     */
    public boolean zsSet(String key, Set<? extends ZSetOperations.TypedTuple> values, long time) {
        return zsSet(key, values, time, TimeUnit.SECONDS);
    }

    /**
     * 将数据放入sort set缓存
     *
     * @param key
     * @param values
     * @param time   秒
     * @return 成功
     */
    public boolean zsSet(String key, Set<? extends ZSetOperations.TypedTuple> values, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                Set set = values.stream().collect(Collectors.toSet());
                redisOperations.opsForZSet().add(key, set);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }

    /**
     * 获取sort set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long zsSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取分值
     *
     * @param key
     * @param value
     * @return 不在排名中返回null
     */
    public Double zsScore(String key, Object value) {
        Double score = redisTemplate.opsForZSet().score(key, value);
        return score;
    }

    /**
     * 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public Set<Object> zsRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsRange(String key, long start, long end, Class<T> tClass) {
        return this.toJavaSet(zsRange(key, start, end), tClass);
    }

    /**
     * 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public Set<ZSetOperations.TypedTuple> zsRangeWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end).stream().collect(Collectors.toSet());
    }

    /**
     * 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsRangeWithScore(String key, long start, long end, Class<T> tClass) {
        return this.toJavaSet(zsRangeWithScore(key, start, end), tClass);
    }

    /**
     * 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public Set<Object> zsRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public Set<Object> zsRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    /**
     * 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsRangeByScore(String key, double min, double max, Class<T> tClass) {
        return this.toJavaSet(zsRangeByScore(key, min, max), tClass);
    }

    /**
     * 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsRangeByScore(String key, double min, double max, long offset, long count, Class<T> tClass) {
        return this.toJavaSet(zsRangeByScore(key, min, max, offset, count), tClass);
    }

    /**
     * 降序 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public Set<Object> zsReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 降序 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsReverseRange(String key, long start, long end, Class<T> tClass) {
        return this.toJavaSet(zsReverseRange(key, start, end), tClass);
    }


    /**
     * 降序 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public Set<ZSetOperations.TypedTuple> zsReverseRangeWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end).stream().collect(Collectors.toSet());
    }

    /**
     * 降序 获取指定排名范围的数据 0为第一个 负数表示倒数取(-1=maxIndex -2=maxIndex-1)
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsReverseRangeWithScore(String key, long start, long end, Class<T> tClass) {
        return this.toJavaSet(zsReverseRangeWithScore(key, start, end), tClass);
    }

    /**
     * 降序 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public Set<Object> zsReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 降序 获取score 区间值的 sort set 闭区间
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> zsReverseRangeByScore(String key, double min, double max, Class<T> tClass) {
        return this.toJavaSet(zsReverseRangeByScore(key, min, max), tClass);
    }

    /**
     * 按value移除
     *
     * @param key    键
     * @param values 值
     * @return 移除的个数
     */
    public long zsRemove(String key, Set<?> values) {
        Long count = redisTemplate.opsForZSet().remove(key, values.toArray());
        return count;
    }

    //    /**
    //     * 按value移除
    //     *
    //     * 一定要记得区分 集合类型和单个value的区别
    //     *
    //     * @param key    键
    //     * @param value 值
    //     * @return 移除的个数
    //     */
    //    public long zsRemove(String key, Object value) {
    //        Long count = redisTemplate.opsForZSet().remove(key, value);
    //        return count;
    //    }

    /**
     * 按value移除
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long zsRemove(String key, Object[] values) {
        Long count = redisTemplate.opsForZSet().remove(key, values);
        return count;
    }

    /**
     * 按分值移除 闭区间
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zsRemoveByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 按排名移除
     *
     * @param key
     * @param start 0为第一个 负数表示倒数取(-1=index -2=index-1)
     * @param end   0为第一个 负数表示倒数取(-1=index -2=index-1)
     * @return
     */
    public long zsRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /* ******************************************************** sortSet end ************************************************************/
    /* ******************************************************** queue start ************************************************************/

    /**
     * 获取all缓存的内容
     *
     * @param key 键
     * @return
     */
    public List lGetAll(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取all缓存的内容
     *
     * @param key 键
     * @return
     */
    public <T> List<T> lGetAll(String key, Class<T> tClass) {
        return this.toJavaList(lGetAll(key), tClass);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始 0 从0开始
     * @param end   结束 -1 倒数第n个
     * @return
     */
    public List lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, long start, long end, Class<T> tClass) {
        return this.toJavaList(lGet(key, start, end), tClass);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lIndex(String key, long index, Class<T> tClass) {
        return this.redisTemplate.toJavaBean(lIndex(key, index), tClass);
    }

    /**
     * 全量放入，会清除原有
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List value) {
        return lSet(key, value, 0);
    }

    /**
     * 全量放入，会清除原有
     *
     * @param key
     * @param value
     * @param time  秒
     * @return
     */
    public boolean lSet(String key, List value, long time) {
        return lSet(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 全量放入，会清除原有
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List value, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                redisOperations.delete(Arrays.asList(key));
                redisOperations.opsForList().rightPushAll(key, value);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }

    /**
     * 添加元素
     *
     * @param key      键
     * @param value    值
     * @param left     插入/追加
     * @param time     时间
     * @param timeUnit
     * @return
     */
    private boolean lAdd(String key, Object value, boolean left, long time, TimeUnit timeUnit) {
        return lAdd(key, Arrays.asList(value), left, time, timeUnit);
    }

    /**
     * 添加元素
     *
     * @param key      键
     * @param value    值
     * @param left     插入/追加
     * @param time     时间
     * @param timeUnit
     * @return
     */
    private boolean lAdd(String key, List value, boolean left, long time, TimeUnit timeUnit) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                if (left) {
                    redisOperations.opsForList().leftPushAll(key, value);
                } else {
                    redisOperations.opsForList().rightPushAll(key, value);
                }
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return true;
    }


    /**
     * 放入单个缓存 头部插入
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lInsert(String key, Object value) {
        return lInsert(key, value, 0);
    }

    /**
     * 放入单个缓存 头部插入
     *
     * @param key
     * @param value
     * @param time  秒
     * @return
     */
    public boolean lInsert(String key, Object value, long time) {
        return lInsert(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 放入单个缓存 头部插入
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    public boolean lInsert(String key, Object value, long time, TimeUnit timeUnit) {
        return lAdd(key, value, true, time, timeUnit);
    }

    /**
     * 将list放入缓存 头部插入
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lInsert(String key, List value) {
        return lInsert(key, value, 0);
    }

    /**
     * 将list放入缓存 头部插入
     *
     * @param key   键
     * @param value 值
     * @param time  秒
     * @return
     */
    public boolean lInsert(String key, List value, long time) {
        return lInsert(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 将list放入缓存 头部插入
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return
     */
    public boolean lInsert(String key, List value, long time, TimeUnit timeUnit) {
        return lAdd(key, value, true, time, timeUnit);
    }

    /**
     * 放入单个缓存 尾部追加
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, Object value) {
        return lPush(key, value, 0);
    }

    /**
     * 放入单个缓存 尾部追加
     *
     * @param key   键
     * @param value 值
     * @param time  秒
     * @return
     */
    public boolean lPush(String key, Object value, long time) {
        return lPush(key, value, time, TimeUnit.SECONDS);
    }


    /**
     * 放入单个缓存 尾部追加
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return
     */
    public boolean lPush(String key, Object value, long time, TimeUnit timeUnit) {
        return lAdd(key, value, false, time, timeUnit);
    }

    /**
     * 将list放入缓存 尾部追加
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, List value) {
        return lPush(key, value, 0);
    }

    /**
     * 将list放入缓存 尾部追加
     *
     * @param key   键
     * @param value 值
     * @param time  秒
     * @return
     */
    public boolean lPush(String key, List value, long time) {
        return lPush(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 将list放入缓存 尾部追加
     *
     * @param key      键
     * @param value    值
     * @param time     时间
     * @param timeUnit
     * @return
     */
    public boolean lPush(String key, List value, long time, TimeUnit timeUnit) {
        return lAdd(key, value, false, time, timeUnit);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdate(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
        return true;
    }

    /**
     * 截取数组 保留start-end范围数据 start end传值疑问，可以直接搜ltrim功能参数
     *
     * @param key   键
     * @param start 开始 0 从0开始
     * @param end   结束 -1 倒数第n个
     * @return
     */
    public boolean lSubList(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
        return true;
    }

    /**
     * 截取数组 保留start-end范围数据 start end传值疑问，可以直接搜ltrim功能参数
     *
     * @param key   键
     * @param start 开始 0 从0开始
     * @param end   结束 -1 倒数第n个
     * @return
     */
    public boolean lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
        return true;
    }

    /**
     * 移除N个值为value的元素
     *
     * @param key   键
     * @param count 0 移除所有 >0 顺序移除N个 <0 倒序移除N个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        Long remove = redisTemplate.opsForList().remove(key, count, value);
        return remove;
    }

    /**
     * 顺序移除首个值为value的元素
     *
     * @param key   键
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, Object value) {
        Long remove = lRemove(key, 1, value);
        return remove;
    }

    /**
     * 返回首个元素
     *
     * @param key
     * @return
     */
    public Object lPeek(String key) {
        Object value = redisTemplate.opsForList().index(key, 0);
        return value;
    }

    /**
     * 返回首个元素
     *
     * @param key
     * @return
     */
    public <T> T lPeek(String key, Class<T> tClass) {
        Object value = lPeek(key);
        return redisTemplate.toJavaBean(value, tClass);
    }

    /**
     * 移除并返回首个元素
     *
     * @param key
     * @return
     */
    public Object lPoll(String key) {
        Object value = redisTemplate.opsForList().leftPop(key);
        return value;
    }

    /**
     * 移除并返回首个元素
     *
     * @param key
     * @return
     */
    public <T> T lPoll(String key, Class<T> tClass) {
        Object value = lPoll(key);
        return redisTemplate.toJavaBean(value, tClass);
    }

    /**
     * 移除并返回末尾元素
     *
     * @param key
     * @return
     */
    public Object lTail(String key) {
        Object value = redisTemplate.opsForList().rightPop(key);
        return value;
    }

    /**
     * 移除并返回末尾元素
     *
     * @param key
     * @return
     */
    public <T> T lTail(String key, Class<T> tClass) {
        Object value = lTail(key);
        return redisTemplate.toJavaBean(value, tClass);
    }

    /* ******************************************************** queue end ************************************************************/
    /* ******************************************************** geo start ************************************************************/

    /**
     * 添加位置信息
     *
     * @param key
     * @param point
     * @param data  元素值 推荐简单格式
     * @return 成功添加个数
     */
    public Long gAdd(String key, Point point, Object data) {
        return gAdd(key, point, data, 0);
    }

    /**
     * 批量添加位置信息
     *
     * @param key
     * @param batchData
     * @return 成功添加个数
     */
    public Long gAdd(String key, Map<Point, ?> batchData) {
        return gAdd(key, batchData, 0);
    }

    /**
     * 添加位置信息
     *
     * @param key
     * @param point
     * @param data  元素值 推荐简单格式
     * @param time
     * @return 成功添加个数
     */
    public Long gAdd(String key, Point point, Object data, long time) {
        return gAdd(key, point, data, time, TimeUnit.SECONDS);
    }

    /**
     * 批量添加位置信息
     *
     * @param key
     * @param batchData
     * @return 成功添加个数
     */
    public Long gAdd(String key, Map<Point, ?> batchData, long time) {
        return gAdd(key, batchData, time, TimeUnit.SECONDS);
    }

    /**
     * 添加位置信息
     *
     * @param key
     * @param point
     * @param data  元素值 推荐简单格式
     * @param time
     * @return 成功添加个数
     */
    public Long gAdd(String key, Point point, Object data, long time, TimeUnit timeUnit) {
        List<Object> list = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                redisOperations.opsForGeo().add(key, point, data);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return (Long) list.get(0);
    }


    /**
     * 批量添加位置信息
     *
     * @param key
     * @param batchData
     * @param time
     * @return 成功添加个数
     */
    public Long gAdd(String key, Map<Point, ?> batchData, long time, TimeUnit timeUnit) {
        List<Object> list = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                long expire = time <= 0 ? -1L : time;
                Map<Object, Point> map = new HashMap<>();
                batchData.forEach((k, v) -> {
                    map.put(v, k);
                });
                redisOperations.opsForGeo().add(key, map);
                if (expire > 0) {
                    redisOperations.expire(key, expire, timeUnit);
                }
                return null;
            }
        });
        return (Long) list.get(0);
    }

    /**
     * 获取俩元素间距信息
     *
     * @param key
     * @param data1
     * @param data2
     * @return
     */
    public Distance gDistance(String key, Object data1, Object data2) {
        return redisTemplate.opsForGeo().distance(key, data1, data2);
    }

    /**
     * 获取俩元素间距信息
     *
     * @param key
     * @param data1
     * @param data2
     * @return
     */
    public Distance gDistance(String key, Object data1, Object data2, Metrics metric) {
        return redisTemplate.opsForGeo().distance(key, data1, data2, metric);
    }

    /**
     * 返回已有元素中的geo专有的hash值
     *
     * @param key
     * @param datas
     * @return
     */
    public List<String> gHash(String key, Collection<?> datas) {
        return redisTemplate.opsForGeo().hash(key, datas.toArray());
    }

    /**
     * 返回已有datas中的位置信息
     *
     * @param key
     * @param datas
     * @return
     */
    public List<Point> gPosition(String key, Collection<?> datas) {
        return redisTemplate.opsForGeo().position(key, datas.toArray());
    }

    /**
     * 给定的坐标，指定半径获取匹配的元素,仅获取元素值 默认由近到远
     *
     * @param key
     * @param within 范围限定条件
     * @param tClass 目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<T> gRadiusSimple(String key, Circle within, Class<T> tClass) {
        return gRadiusSimple(key, within, tClass, null, Sort.Direction.ASC);
    }

    /**
     * 给定的坐标，指定半径获取匹配的元素,仅获取元素值
     *
     * @param key
     * @param within    范围限定条件
     * @param tClass    目标对象
     * @param direction null 乱序
     * @param limit     null 全部
     * @return 包含统计信息的结果集
     */
    public <T> List<T> gRadiusSimple(String key, Circle within, Class<T> tClass, Long limit, Sort.Direction direction) {
        GeoResults<GeoLocation<T>> results = this.gRadius(key, within, direction, limit, false, false, tClass);
        List<T> list = results.getContent().stream().map(item -> item.getContent().getName())
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 给定的坐标，指定半径获取匹配的位置信息 默认由近到远
     *
     * @param key
     * @param within 范围限定条件
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoLocation<T>> gRadiusLocation(String key, Circle within, Class<T> tClass) {
        return gRadiusLocation(key, within, tClass, null, Sort.Direction.ASC);
    }

    /**
     * 给定的坐标，指定半径获取匹配的位置信息
     *
     * @param key
     * @param within    范围限定条件
     * @param direction null 乱序
     * @param limit     null 全部
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoLocation<T>> gRadiusLocation(String key, Circle within, Class<T> tClass, Long limit,
                                                                     Sort.Direction direction) {
        GeoResults<GeoLocation<T>> results = this.gRadius(key, within, direction, limit, false, false, tClass);
        List<GeoLocation<T>> list = results.getContent().stream().map(GeoResult::getContent).collect(Collectors.toList());
        return list;
    }

    /**
     * 给定的坐标，指定半径获取距离、位置等内容的结果集 默认由近到远
     *
     * @param key
     * @param within 范围限定条件
     * @param tClass 目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoResult<GeoLocation<T>>> gRadiusDistAndLoc(String key, Circle within, Class<T> tClass) {
        return gRadiusDistAndLoc(key, within, tClass, null, Sort.Direction.ASC);
    }

    /**
     * 给定的坐标，指定半径获取距离、位置等内容的结果集
     *
     * @param key
     * @param within    范围限定条件
     * @param tClass    目标对象
     * @param direction null 乱序
     * @param limit     null 全部
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoResult<GeoLocation<T>>> gRadiusDistAndLoc(String key, Circle within, Class<T> tClass, Long limit,
                                                                                  Sort.Direction direction) {
        GeoResults<GeoLocation<T>> results = this.gRadius(key, within, direction, limit, false, false, tClass);
        return results.getContent();
    }

    /**
     * 给定的坐标，指定半径获取匹配的geo信息 默认由近到远
     *
     * @param key
     * @param within 范围限定条件
     * @return 包含统计信息、距离、位置等内容的结果集
     */
    public <T> GeoResults<GeoLocation<T>> gRadius(String key, Circle within, Class<T> tClass) {
        return this.gRadius(key, within, Sort.Direction.ASC, null, true, true, tClass);
    }

    /**
     * 给定的坐标，指定半径获取匹配的geo信息 默认由近到远
     *
     * @param key
     * @param within 范围限定条件
     * @return 包含统计信息、距离、位置等内容的结果集
     */
    public <T> GeoResults<GeoLocation<T>> gRadius(String key, Circle within, Long limit, Sort.Direction direction,
                                                                   Class<T> tClass) {
        return this.gRadius(key, within, direction, limit, true, true, tClass);
    }

    /**
     * 给定的坐标，指定半径获取匹配的内容
     *
     * @param key
     * @param within       范围限定条件
     * @param direction    null 乱序
     * @param limit        null 全部
     * @param withPoint    是否包含位置信息
     * @param withDistance 是否包含距离信息
     * @param tClass       目标对象
     * @return 包含统计信息的结果集
     */
    private <T> GeoResults<GeoLocation<T>> gRadius(String key, Circle within, Sort.Direction direction, Long limit,
                                                                    boolean withPoint, boolean withDistance, Class<T> tClass) {
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs();
        if (direction != null) {
            if (direction == Sort.Direction.ASC) {
                geoRadiusCommandArgs.sortAscending();
            } else {
                geoRadiusCommandArgs.sortDescending();
            }
        }
        if (limit != null && limit > 0) {
            geoRadiusCommandArgs.limit(limit);
        }
        if (withPoint) {
            geoRadiusCommandArgs.includeCoordinates();
        }
        if (withDistance) {
            geoRadiusCommandArgs.includeDistance();
        }

        GeoResults<RedisGeoCommands.GeoLocation<Object>> radius = redisTemplate.opsForGeo().radius(key, within, geoRadiusCommandArgs);
        GeoResults<GeoLocation<T>> geoResults = gResultsConvert(radius, tClass);
        return geoResults;
    }

    private <T> GeoResults<GeoLocation<T>> gResultsConvert(GeoResults<RedisGeoCommands.GeoLocation<Object>> results, Class<T> tClass) {
        List<GeoResult<GeoLocation<T>>> list = results.getContent().stream().map(item -> {
            GeoLocation<Object> location = item.getContent();
            T targetItem = redisTemplate.toJavaBean(location.getName(), tClass);
            GeoLocation<T> geoLocation = new GeoLocation<>(targetItem, location.getPoint());
            GeoResult<GeoLocation<T>> geoResult = new GeoResult<>(geoLocation, item.getDistance());
            return geoResult;
        }).collect(Collectors.toList());
        GeoResults<GeoLocation<T>> geoResults = new GeoResults<>(list, results.getAverageDistance());
        return geoResults;
    }

    /**
     * 从位置集合中中的某个元素，指定半径获取匹配的geo信息 默认由近到远
     *
     * @param key
     * @param geoResult 已有元素值
     * @return 包含统计信息、距离、位置等内容的结果集
     */
    public <T> GeoResults<GeoLocation<T>> gRadius(String key, GeoResult<T> geoResult, Class<T> tClass) {
        return this.gRadius(key, geoResult.getContent(), geoResult.getDistance(), Sort.Direction.ASC, null, true, true,
                tClass);
    }

    /**
     * 从位置集合中中的某个元素，指定半径获取匹配的geo信息
     *
     * @param key
     * @param geoResult 已有元素值
     * @return 包含统计信息、距离、位置等内容的结果集
     */
    public <T> GeoResults<GeoLocation<T>> gRadius(String key, GeoResult<T> geoResult, Long limit,
                                                                   Sort.Direction direction, Class<T> tClass) {
        return this.gRadius(key, geoResult.getContent(), geoResult.getDistance(), direction, limit, true, true, tClass);
    }

    /**
     * 从位置集合中中的某个元素，指定半径获取匹配的元素,仅获取元素值 默认由近到远
     *
     * @param key
     * @param geoResult 已有元素值
     * @param tClass    目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<T> gRadiusSimple(String key, GeoResult<T> geoResult, Class<T> tClass) {
        return gRadiusSimple(key, geoResult, null, Sort.Direction.ASC, tClass);
    }

    /**
     * 从位置集合中中的某个元素，指定半径获取匹配的元素,仅获取元素值
     *
     * @param key
     * @param geoResult 已有元素值
     * @param tClass    目标对象
     * @param direction null 乱序
     * @param limit     null 全部
     * @return 包含统计信息的结果集
     */
    public <T> List<T> gRadiusSimple(String key, GeoResult<T> geoResult, Long limit, Sort.Direction direction,
                                     Class<T> tClass) {
        GeoResults<GeoLocation<T>> results = this
                .gRadius(key, geoResult.getContent(), geoResult.getDistance(), direction, limit, false, false, tClass);
        List<T> list = results.getContent().stream().map(item -> item.getContent().getName())
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 给定的坐标，指定半径获取匹配的位置信息 默认由近到远
     *
     * @param key
     * @param geoResult 已有元素值
     * @param tClass    目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoLocation<T>> gRadiusLocation(String key, GeoResult<T> geoResult, Class<T> tClass) {
        return gRadiusLocation(key, geoResult, null, Sort.Direction.ASC, tClass);
    }

    /**
     * 给定的坐标，指定半径获取匹配的位置信息
     *
     * @param key
     * @param geoResult 已有元素值
     * @param direction null 乱序
     * @param limit     null 全部
     * @param tClass    目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoLocation<T>> gRadiusLocation(String key, GeoResult<T> geoResult, Long limit,
                                                                     Sort.Direction direction, Class<T> tClass) {
        GeoResults<GeoLocation<T>> results = this
                .gRadius(key, geoResult.getContent(), geoResult.getDistance(), direction, limit, false, false, tClass);
        List<GeoLocation<T>> list = results.getContent().stream().map(GeoResult::getContent)
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 给定的坐标，指定半径获取匹配的距离、位置等内容的结果集 默认由近到远
     *
     * @param key
     * @param geoResult 已有元素值
     * @param tClass    目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoResult<GeoLocation<T>>> gRadiusDistAndLoc(String key, GeoResult<T> geoResult, Class<T> tClass) {
        return gRadiusDistAndLoc(key, geoResult, null, Sort.Direction.ASC, tClass);
    }

    /**
     * 给定的坐标，指定半径获取匹配的距离、位置等内容的结果集
     *
     * @param key
     * @param geoResult 已有元素值
     * @param direction null 乱序
     * @param limit     null 全部
     * @param tClass    目标对象
     * @return 包含统计信息的结果集
     */
    public <T> List<GeoResult<GeoLocation<T>>> gRadiusDistAndLoc(String key, GeoResult<T> geoResult, Long limit,
                                                                                  Sort.Direction direction, Class<T> tClass) {
        GeoResults<GeoLocation<T>> results = this
                .gRadius(key, geoResult.getContent(), geoResult.getDistance(), direction, limit, false, false, tClass);
        return results.getContent();
    }

    /**
     * 从位置集合中中的某个元素，指定半径获取匹配的结果
     *
     * @param key
     * @param one          已有元素值
     * @param distance     距离信息
     * @param direction    null 乱序
     * @param limit        null 全部
     * @param withPoint    是否包含位置信息
     * @param withDistance 是否包含距离信息
     * @param tClass       目标对象
     * @return 包含统计信息的结果集
     */
    private <T> GeoResults<GeoLocation<T>> gRadius(String key, T one, Distance distance, Sort.Direction direction,
                                                                    Long limit, boolean withPoint, boolean withDistance, Class<T> tClass) {
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs();
        if (direction != null) {
            if (direction == Sort.Direction.ASC) {
                geoRadiusCommandArgs.sortAscending();
            } else {
                geoRadiusCommandArgs.sortDescending();
            }
        }
        if (limit != null && limit > 0) {
            geoRadiusCommandArgs.limit(limit);
        }
        if (withPoint) {
            geoRadiusCommandArgs.includeCoordinates();
        }
        if (withDistance) {
            geoRadiusCommandArgs.includeDistance();
        }

        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo()
                .radius(key, one, distance, geoRadiusCommandArgs);
        GeoResults<GeoLocation<T>> geoResults = gResultsConvert(results, tClass);
        return geoResults;
    }


    /**
     * 移除元素
     *
     * @param key
     * @param data
     * @return
     */
    public Long gRemove(String key, Collection<?> data) {
        return redisTemplate.opsForGeo().remove(key, data.toArray());
    }

    //    /**
    //     * 移除元素
    //     *
    //     * 一定要记得区分 集合类型和单个value的区别
    //     *
    //     * @param key
    //     * @param data
    //     * @return
    //     */
    //    public Long gRemove(String key, Object data) {
    //        return redisTemplate.opsForGeo().remove(key, data);
    //    }

    /**
     * 移除元素
     *
     * @param key
     * @param data
     * @return
     */
    public Long gRemove(String key, Object[] data) {
        return redisTemplate.opsForGeo().remove(key, data);
    }


    /* ******************************************************** geo end ************************************************************/
}