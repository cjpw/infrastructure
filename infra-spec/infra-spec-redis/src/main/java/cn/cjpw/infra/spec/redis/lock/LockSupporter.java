package cn.cjpw.infra.spec.redis.lock;

import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;

/**
 * @author jun.chen1
 * @since 2023/1/16 15:41
 **/
public interface LockSupporter<R> {

    /**
     * 获取分布式锁 默认一直等待，直到获取锁
     *
     * @param lockName
     * @return
     */
    R getLock(String lockName);

    /**
     * 获取分布式锁 默认一直等待，直到获取锁
     *
     * @param lockName
     * @return
     */
    R getReadLock(String lockName);

    /**
     * 获取分布式锁 默认一直等待，直到获取锁
     *
     * @param lockName
     * @return
     */
    R getWriteLock(String lockName);

    /**
     * 获取分布式锁 快速定义等待策略
     *
     * @param lockName
     * @return
     */
    R getLock(String lockName, LockExpire lockExpire);

    /**
     * 获取分布式锁 不等待，不关注锁获取失败相关异常
     *
     * @param lockName
     * @return
     */
    R getLockNoWait(String lockName);

    /**
     * 查询任意线程正持有锁
     *
     * @param lockName
     * @return
     */
    boolean hasLock(String lockName);

    /**
     * 查询是否当前线程正持有锁
     *
     * @param lockName
     * @return
     */
    boolean hasLockByCurrent(String lockName);

    /**
     * 查询指定线程是否正持有锁
     *
     * @param lockName
     * @return
     */
    boolean hasLockByThread(String lockName, int threadId);
}
