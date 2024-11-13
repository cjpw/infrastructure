package cn.cjpw.infra.spec.redis;

import cn.cjpw.infra.spec.redis.lock.LockSupporter;
import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
import cn.cjpw.infra.spec.redis.redisson.MultiRedissonLockBuilder;
import cn.cjpw.infra.spec.redis.redisson.SingleRedissonLockBuilder;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * @author jun.chen1
 * @since 2023/1/16 15:35
 **/
public class RedissonLockWrapper implements LockSupporter<SingleRedissonLockBuilder> {

    private RedissonClient redissonClient;

    public RedissonLockWrapper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private void validateLockSupporter() {
        if (redissonClient == null) {
            throw new NullPointerException("未配置RedissonClient");
        }
    }


    @Override
    public SingleRedissonLockBuilder getLock(String lockName) {
        return SingleRedissonLockBuilder.of(lockName, LockExpire.forever()).setRedissonClient(redissonClient);
    }

    @Override
    public SingleRedissonLockBuilder getReadLock(String lockName) {
        return SingleRedissonLockBuilder.of(lockName, LockExpire.forever()).setRedissonClient(redissonClient).lockType(SingleRedissonLockBuilder.READ_LOCK);
    }

    @Override
    public SingleRedissonLockBuilder getWriteLock(String lockName) {
        return SingleRedissonLockBuilder.of(lockName, LockExpire.forever()).setRedissonClient(redissonClient).lockType(SingleRedissonLockBuilder.WRITE_LOCK);
    }

    @Override
    public SingleRedissonLockBuilder getLock(String lockName, LockExpire lockExpire) {
        return SingleRedissonLockBuilder.of(lockName, lockExpire).setRedissonClient(redissonClient);
    }

    @Override
    public SingleRedissonLockBuilder getLockNoWait(String lockName) {
        SingleRedissonLockBuilder lockBuilder = SingleRedissonLockBuilder.of(lockName, LockExpire.noWait())
                .setRedissonClient(redissonClient);
        return lockBuilder;
    }

    public MultiRedissonLockBuilder getMultiLock(List<String> lockNames, LockExpire lockExpire) {
        return MultiRedissonLockBuilder.of(lockNames, lockExpire).setRedissonClient(redissonClient);
    }

    public MultiRedissonLockBuilder getMultiLockNoWait(List<String> lockNames) {
        return MultiRedissonLockBuilder.of(lockNames, LockExpire.noWait()).setRedissonClient(redissonClient);
    }

    @Override
    public boolean hasLock(String lockName) {
        return redissonClient.getLock(lockName).isLocked();
    }

    @Override
    public boolean hasLockByCurrent(String lockName) {
        return redissonClient.getLock(lockName).isHeldByCurrentThread();
    }

    @Override
    public boolean hasLockByThread(String lockName, int threadId) {
        return redissonClient.getLock(lockName).isHeldByThread(threadId);
    }

}
