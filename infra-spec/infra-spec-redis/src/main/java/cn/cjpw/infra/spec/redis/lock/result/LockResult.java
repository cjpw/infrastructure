package cn.cjpw.infra.spec.redis.lock.result;



/**
 * lock之后的结果封装
 *
 * @author jun.chen1
 * @since 2022/1/25 17:25
 **/
public class LockResult<T> {

    /**
     * 是否加锁成功
     */
    private boolean hasLock;
    /**
     * 锁持有线程id
     */
    private long lockHolderId;

    /**
     * 锁实例
     */
    private T lock;

    /**
     * 锁持有线程中断的异常
     */
    private InterruptedException interruptedException;

    /**
     * 加锁后执行业务流程异常
     */
    private Exception execException;


    /**
     * 获取 是否加锁成功
     *
     * @return hasLock 是否加锁成功
     */
    public boolean isHasLock() {
        return this.hasLock;
    }

    /**
     * 设置 是否加锁成功
     *
     * @param hasLock 是否加锁成功
     */
    public void setHasLock(boolean hasLock) {
        this.hasLock = hasLock;
    }

    /**
     * 获取 锁实例
     *
     * @return lock 锁实例
     */
    public T getLock() {
        return this.lock;
    }

    /**
     * 设置 锁实例
     *
     * @param lock 锁实例
     */
    public void setLock(T lock) {
        this.lock = lock;
    }

    /**
     * 获取 锁持有线程中断的异常
     *
     * @return interruptedException 锁持有线程中断的异常
     */
    public InterruptedException getInterruptedException() {
        return this.interruptedException;
    }

    /**
     * 设置 锁持有线程中断的异常
     *
     * @param interruptedException 锁持有线程中断的异常
     */
    public void setInterruptedException(InterruptedException interruptedException) {
        this.interruptedException = interruptedException;
    }

    /**
     * 获取 加锁后执行业务流程异常
     *
     * @return otherException 加锁后执行业务流程异常
     */
    public Exception getExecException() {
        return this.execException;
    }

    /**
     * 设置 加锁后执行业务流程异常
     *
     * @param execException 加锁后执行业务流程异常
     */
    public void setExecException(Exception execException) {
        this.execException = execException;
    }

    /**
     * 获取 锁线程id
     *
     * @return lockThreadId 锁线程id
     */
    public long getLockHolderId() {
        return this.lockHolderId;
    }

    /**
     * 设置 锁线程id
     *
     * @param lockHolderId 锁线程id
     */
    public void setLockHolderId(long lockHolderId) {
        this.lockHolderId = lockHolderId;
    }
}
