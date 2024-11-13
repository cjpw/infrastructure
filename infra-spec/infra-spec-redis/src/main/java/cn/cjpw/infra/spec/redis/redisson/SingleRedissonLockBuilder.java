package cn.cjpw.infra.spec.redis.redisson;

import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
import cn.cjpw.infra.spec.redis.lock.extention.HolderAdditional;
import cn.cjpw.infra.spec.redis.lock.result.SingleLockResult;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 使用lambada语法简化分布式锁工具
 *
 * @author jun.chen1
 * @since 2020/8/25 14:47
 **/
public class SingleRedissonLockBuilder extends AbstractRedissonLockBuilder<SingleRedissonLockBuilder, SingleLockResult<RLock>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleRedissonLockBuilder.class);

    private String lockName;
    private int lockType = NORMAL_LOCK;
    private RLock rLock;
    private HolderAdditional holderAdditional;

    public SingleRedissonLockBuilder() {
        super();
    }

    public SingleRedissonLockBuilder(RedissonClient redissonClient) {
        super(redissonClient);
    }

    /**
     * 构造入口 使用默认策略 {@link this#defaultCallBackStrategy()}
     *
     * @param lockName
     * @return
     */
    public static SingleRedissonLockBuilder of(String lockName) {
        return of(lockName, null);
    }

    /**
     * 构造入口 使用默认策略 {@link this#defaultCallBackStrategy()}
     *
     * @param lockName
     * @return
     */
    public static SingleRedissonLockBuilder of(String lockName, LockExpire lockExpire) {
        SingleRedissonLockBuilder builder = new SingleRedissonLockBuilder();
        builder.lockName = lockName;
        if (lockExpire != null) {
            builder.lockExpire = lockExpire;
        }
        return builder;
    }

    /**
     * 设置锁类型
     *
     * @param lockType
     * @return
     */
    public SingleRedissonLockBuilder lockType(int lockType) {
        this.lockType = lockType;
        return this;
    }

    /**
     * 添加锁成功后的扩展信息
     *
     * @param holderAdditional
     * @return
     */
    public SingleRedissonLockBuilder onSuccessAddition(HolderAdditional holderAdditional) {
        this.holderAdditional = holderAdditional;
        return this;
    }

    @Override
    protected SingleLockResult<RLock> initialLockResult() {
        SingleLockResult<RLock> lockResult = new SingleLockResult<>(this.lockName, s -> {
            RBucket<HolderAdditional> bucket = redissonClient.getBucket(lockName + ":_desc");
            return bucket.get();
        });
        return lockResult;
    }

    @Override
    protected void defaultCallBackStrategy() {
        super.defaultCallBackStrategy();
        this.lockSuccessCallBackWrapper.setBeforeEventConsumer(lr -> {
            if (holderAdditional != null) {
                RBucket<HolderAdditional> bucket = redissonClient.getBucket(lockName + ":_desc");
                if (Objects.isNull(lockExpire.getLeaseTime()) || lockExpire.getLeaseTime() <= 0) {
                    //避免异常原因长期不释放
                    bucket.setAsync(holderAdditional, 1, TimeUnit.DAYS);
                } else {
                    //持有固定时长，与锁默认时间保持一致即可，后续外部对锁生命周期的干预不考虑在范围内
                    bucket.setAsync(holderAdditional, lockExpire.getLeaseTime(), lockExpire.getTimeUnit());
                }
            }
        });
        this.lockFinalCallBackWrapper.setAfterEventConsumer(lr -> {
            if (lr.isHasLock() && holderAdditional != null) {
                RBucket<Object> bucket = redissonClient.getBucket(lockName + ":_desc");
                bucket.deleteAsync();
            }
        });
    }

    /**
     * 根据加锁策略创建一个未加锁实例
     *
     * @return
     */
    @Override
    public RLock build() {
        RLock rLock = this.rLock;
        if (Objects.isNull(rLock)) {
            if (lockType == NORMAL_LOCK) {
                rLock = redissonClient.getLock(this.lockName);
            } else if (lockType == FAIR_LOCK) {
                rLock = redissonClient.getFairLock(this.lockName);
            } else if (lockType == READ_LOCK) {
                rLock = redissonClient.getReadWriteLock(this.lockName).writeLock();
            } else if (lockType == WRITE_LOCK) {
                rLock = redissonClient.getReadWriteLock(this.lockName).readLock();
            } else {
                throw new UnsupportedOperationException("不支持的锁类型");
            }
            this.rLock = rLock;
        }
        return rLock;
    }

}
