package cn.cjpw.infra.spec.redis;


import cn.cjpw.infra.spec.json.JsonUtil;
import cn.cjpw.infra.spec.base.type.TypeReference;
import cn.hutool.core.util.StrUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * redis缓存场景扩展
 * 用于缓存击穿、穿透、雪崩等场景
 *
 * @author jun.chen1
 * @since 2021/10/20 10:56
 **/
public class PriorityCache {

    private RedisUtil redisUtil;

    private static final ExecutorService refreshPool = Executors.newFixedThreadPool(4);

    PriorityCache(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 放置一个防止被击穿的缓存。
     * 使用改接口设置一个maxAliveSeconds时长的缓存。未超过validSeconds时长时，始终命中缓存。超过validSeconds时长时，优先命中缓存，但提供相应参数提醒调用方刷新缓存
     *
     * @param protectedCacheStrategy 缓存策略
     * @param value                  缓存内容
     */
    public void putProtectedCache(ProtectedCacheStrategy protectedCacheStrategy, Object value) {
        ProtectedCache protectedCache = new ProtectedCache();
        protectedCache.setData(JsonUtil.toJsonStr(value));
        protectedCache.setInvalidTimeMillis(System.currentTimeMillis() + protectedCacheStrategy.getValidSeconds() * 1000);
        redisUtil.set(protectedCacheStrategy.getKey(), protectedCache, protectedCacheStrategy.getMaxAliveSeconds());
    }

    /**
     * 获取缓存，如果不满足缓存配置策略，则异步使用refresh加载最新数据并写入缓存
     * 所有参数要求必填
     *
     * @param protectedCacheStrategy 缓存配置策略
     * @param typeReference          目标泛型
     * @param refresh                获取最新数据的接口
     * @return
     */
    public <T> T getProtectedCache(ProtectedCacheStrategy protectedCacheStrategy, TypeReference<T> typeReference, Supplier<T> refresh) {
        ProtectedCache protectedCache = redisUtil.get(protectedCacheStrategy.getKey(), ProtectedCache.class);
        if (protectedCache == null) {
            T value = refresh.get();
            putProtectedCache(protectedCacheStrategy, value);
            return value;
        } else {
            if (protectedCache.getInvalidTimeMillis() <= System.currentTimeMillis()) {
                todoRefresh(protectedCacheStrategy, refresh);
            }
            return JsonUtil.toBean(protectedCache.getData(), typeReference);
        }
    }

    private <T> void todoRefresh(ProtectedCacheStrategy protectedCacheStrategy, Supplier<T> refresh) {
        if (StrUtil.isNotBlank(protectedCacheStrategy.getLockKeyWhenRefresh())) {
            redisUtil.getLockNoWait(protectedCacheStrategy.getLockKeyWhenRefresh())
                    .submitNr(lock -> exec(protectedCacheStrategy, refresh));
        } else {
            exec(protectedCacheStrategy, refresh);
        }
    }

    private <T> void exec(ProtectedCacheStrategy protectedCacheStrategy, Supplier<T> refresh) {
        refreshPool.submit(() -> {
            T value = refresh.get();
            putProtectedCache(protectedCacheStrategy, value);
        });
    }


    private static class ProtectedCache {
        /**
         * 缓存返回值
         */
        private String data;
        /**
         * 有效性的到期时间戳
         */
        private long invalidTimeMillis;


        /**
         * 获取 缓存返回值
         *
         * @return data 缓存返回值
         */
        public String getData() {
            return this.data;
        }

        /**
         * 设置 缓存返回值
         *
         * @param data 缓存返回值
         */
        public void setData(String data) {
            this.data = data;
        }

        /**
         * 获取 有效性的到期时间戳
         *
         * @return invalidTimeMillis 有效性的到期时间戳
         */
        public long getInvalidTimeMillis() {
            return this.invalidTimeMillis;
        }

        /**
         * 设置 有效性的到期时间戳
         *
         * @param invalidTimeMillis 有效性的到期时间戳
         */
        public void setInvalidTimeMillis(long invalidTimeMillis) {
            this.invalidTimeMillis = invalidTimeMillis;
        }
    }

    /**
     * 缓存处理策略配置类
     */
    public static class ProtectedCacheStrategy {
        /**
         * 缓存key
         */
        private String key;
        /**
         * 加锁key，如果执行拿新数据流程耗时比较长的话，防止此段时间仍然有大量请求执行刷新操作
         */
        private String lockKeyWhenRefresh;
        /**
         * 缓存最大存活时长
         */
        private long maxAliveSeconds;
        /**
         * 缓存写入后，能保证有效性的时长标记 秒
         */
        private long validSeconds;

        /**
         * 获取 缓存key
         *
         * @return key 缓存key
         */
        public String getKey() {
            return this.key;
        }

        /**
         * 设置 缓存key
         *
         * @param key 缓存key
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * 获取 加锁key，如果执行拿新数据流程耗时比较长的话，防止此段时间仍然有大量请求执行刷新操作
         *
         * @return lockKeyWhenRefresh 加锁key，如果执行拿新数据流程耗时比较长的话，防止此段时间仍然有大量请求执行刷新操作
         */
        public String getLockKeyWhenRefresh() {
            return this.lockKeyWhenRefresh;
        }

        /**
         * 设置 加锁key，如果执行拿新数据流程耗时比较长的话，防止此段时间仍然有大量请求执行刷新操作
         *
         * @param lockKeyWhenRefresh 加锁key，如果执行拿新数据流程耗时比较长的话，防止此段时间仍然有大量请求执行刷新操作
         */
        public void setLockKeyWhenRefresh(String lockKeyWhenRefresh) {
            this.lockKeyWhenRefresh = lockKeyWhenRefresh;
        }

        /**
         * 获取 缓存最大存活时长
         *
         * @return maxAliveSeconds 缓存最大存活时长
         */
        public long getMaxAliveSeconds() {
            return this.maxAliveSeconds;
        }

        /**
         * 设置 缓存最大存活时长
         *
         * @param maxAliveSeconds 缓存最大存活时长
         */
        public void setMaxAliveSeconds(long maxAliveSeconds) {
            this.maxAliveSeconds = maxAliveSeconds;
        }

        /**
         * 获取 缓存写入后，能保证有效性的时长标记 秒
         *
         * @return validSeconds 缓存写入后，能保证有效性的时长标记 秒
         */
        public long getValidSeconds() {
            return this.validSeconds;
        }

        /**
         * 设置 缓存写入后，能保证有效性的时长标记 秒
         *
         * @param validSeconds 缓存写入后，能保证有效性的时长标记 秒
         */
        public void setValidSeconds(long validSeconds) {
            this.validSeconds = validSeconds;
        }
    }

}
