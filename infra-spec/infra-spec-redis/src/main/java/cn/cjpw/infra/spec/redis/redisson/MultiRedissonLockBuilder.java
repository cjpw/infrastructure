package cn.cjpw.infra.spec.redis.redisson;

import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
import cn.cjpw.infra.spec.redis.lock.extention.HolderAdditional;
import cn.cjpw.infra.spec.redis.lock.result.MultiLockResult;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatch;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author jun.chen1
 * @since 2023/5/15 18:19
 **/
public class MultiRedissonLockBuilder extends AbstractRedissonLockBuilder<MultiRedissonLockBuilder, MultiLockResult<RLock>> {

    private Map<String, Integer> lockNameMap;
    private Map<String, HolderAdditional> holderAdditionMap;

    RLock rLock;

    public MultiRedissonLockBuilder(int lockSize) {
        super();
        lockNameMap = new HashMap<>(lockSize);
        holderAdditionMap = new HashMap<>(lockSize);
    }

    public MultiRedissonLockBuilder(int lockSize, RedissonClient redissonClient) {
        super(redissonClient);
        lockNameMap = new HashMap<>(lockSize);
        holderAdditionMap = new HashMap<>(lockSize);
    }


    @Override
    protected MultiLockResult<RLock> initialLockResult() {
        MultiLockResult<RLock> lockResult = new MultiLockResult<>(new ArrayList<>(this.lockNameMap.keySet()),this.redissonClient);
        return lockResult;
    }

    @Override
    protected void defaultCallBackStrategy() {
        super.defaultCallBackStrategy();
        this.lockSuccessCallBackWrapper.setBeforeEventConsumer(lr -> {
            if (holderAdditionMap != null && !holderAdditionMap.isEmpty()) {
                BatchOptions batchOptions = BatchOptions.defaults().skipResult();
                RBatch batch = redissonClient.createBatch(batchOptions);
                for (Map.Entry<String, HolderAdditional> entry : holderAdditionMap.entrySet()) {
                    if (Objects.isNull(lockExpire.getLeaseTime()) || lockExpire.getLeaseTime() <= 0) {
                        //避免异常原因长期不释放
                        batch.getBucket(entry.getKey() + ":_desc").setAsync(entry.getValue(), 1, TimeUnit.DAYS);
                    } else {
                        //持有固定时长，与锁默认时间保持一致即可，后续外部对锁生命周期的干预不考虑在范围内
                        batch.getBucket(entry.getKey() + ":_desc").setAsync(entry.getValue(), lockExpire.getLeaseTime(), lockExpire.getTimeUnit());
                    }
                }
                batch.executeAsync();
            }
        });
        this.lockFinalCallBackWrapper.setAfterEventConsumer(lr -> {
            if (lr.isHasLock() && holderAdditionMap != null && !holderAdditionMap.isEmpty()) {
                redissonClient.getKeys().deleteAsync(holderAdditionMap.keySet().stream().map(lockName -> lockName + ":_desc").toArray(String[]::new));
            }
        });
    }

    /**
     * 添加锁成功后的扩展信息
     *
     * @param holderAdditional
     * @return
     */
    public MultiRedissonLockBuilder onSuccessAddition(String lockName, HolderAdditional holderAdditional) {
        if (holderAdditionMap == null) {
            holderAdditionMap = new HashMap<>(lockNameMap.size());
        }
        holderAdditionMap.put(lockName, holderAdditional);
        return this;
    }

    /**
     * 添加锁成功后的扩展信息
     *
     * @param map
     * @return
     */
    public MultiRedissonLockBuilder onSuccessAdditionAll(Map<String, HolderAdditional> map) {
        if (holderAdditionMap == null) {
            holderAdditionMap = new HashMap<>(lockNameMap.size());
        }
        holderAdditionMap.putAll(map);
        return this;
    }

    /**
     * 构造入口 使用默认策略 {@link this#defaultCallBackStrategy()}
     *
     * @param lockNames
     * @return
     */
    public static MultiRedissonLockBuilder of(List<String> lockNames) {
        return of(lockNames, null);
    }

    /**
     * 联锁构造入口,使用相同的加锁策略
     *
     * @param lockNames
     * @return
     */
    public static MultiRedissonLockBuilder of(List<String> lockNames, LockExpire lockExpire) {
        MultiRedissonLockBuilder builder = new MultiRedissonLockBuilder(lockNames.size());
        for (String lockName : lockNames) {
            builder.lockNameMap.put(lockName, NORMAL_LOCK);
        }
        if (lockExpire != null) {
            builder.lockExpire = lockExpire;
        }
        return builder;
    }

    /**
     * 新加入一个锁
     *
     * @param lockName
     * @return
     */
    public MultiRedissonLockBuilder and(String lockName) {
        this.lockNameMap.put(lockName, NORMAL_LOCK);
        return this;
    }

    /**
     * 新加入一个锁,如果锁名已存在则会直接覆盖
     *
     * @param lockName
     * @param lockType
     * @return
     */
    public MultiRedissonLockBuilder and(String lockName, int lockType) {
        this.lockNameMap.put(lockName, lockType);
        return this;
    }

    /**
     * 根据加锁策略创建一个未加锁实例
     *
     * @return
     */
    @Override
    public RLock build() {
        if (Objects.isNull(this.rLock)) {
            RLock[] locks = lockNameMap.entrySet().stream().map(item -> {
                String lockName = item.getKey();
                int lockType = item.getValue();
                RLock lock = null;
                if (Objects.isNull(rLock)) {
                    if (lockType == NORMAL_LOCK) {
                        lock = redissonClient.getLock(lockName);
                    } else if (lockType == FAIR_LOCK) {
                        lock = redissonClient.getFairLock(lockName);
                    } else if (lockType == READ_LOCK) {
                        lock = redissonClient.getReadWriteLock(lockName).writeLock();
                    } else if (lockType == WRITE_LOCK) {
                        lock = redissonClient.getReadWriteLock(lockName).readLock();
                    } else {
                        throw new UnsupportedOperationException("不支持的锁类型");
                    }
                }
                return lock;
            }).toArray(RLock[]::new);
            RLock multiLock = redissonClient.getMultiLock(locks);
            this.rLock = multiLock;
        }
        return this.rLock;
    }
}
