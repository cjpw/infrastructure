package cn.cjpw.infra.spec.redis.redisson;

import cn.cjpw.infra.spec.redis.lock.bean.LockExpire;
import cn.cjpw.infra.spec.redis.lock.callback.ExecErrorCallBack;
import cn.cjpw.infra.spec.redis.lock.callback.LockEventCallBackWrapper;
import cn.cjpw.infra.spec.redis.lock.callback.LockFailCallBack;
import cn.cjpw.infra.spec.redis.lock.callback.LockFinalCallBack;
import cn.cjpw.infra.spec.redis.lock.result.LockResult;
import cn.cjpw.infra.spec.redis.redisson.callback.impl.DoNothingCallBack;
import cn.cjpw.infra.spec.redis.redisson.callback.impl.FastUnlockErrorCallBack;
import cn.cjpw.infra.spec.redis.redisson.callback.impl.FastUnlockFinalCallBack;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author jun.chen1
 * @since 2023/5/15 19:53
 **/
public abstract class AbstractRedissonLockBuilder<Builder extends AbstractRedissonLockBuilder<Builder, LR>, LR extends LockResult<RLock>> implements RedissonLockBuilder<LR, Builder> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleRedissonLockBuilder.class);

    protected RedissonClient redissonClient;
    protected LockExpire lockExpire;
    protected LockEventCallBackWrapper<LR> lockFinalCallBackWrapper;
    protected LockEventCallBackWrapper<LR> lockFailCallBackWrapper;
    protected LockEventCallBackWrapper<LR> execErrorCallBackWrapper;
    protected LockEventCallBackWrapper<LR> lockSuccessCallBackWrapper;

    /**
     * lockType 互斥锁
     */
    public static final int NORMAL_LOCK = 0;
    /**
     * lockType 公平斥锁
     */
    public static final int FAIR_LOCK = 1;
    /**
     * lockType 写锁
     */
    public static final int WRITE_LOCK = 2;
    /**
     * lockType 读锁
     */
    public static final int READ_LOCK = 3;


    public AbstractRedissonLockBuilder() {
        defaultCallBackStrategy();
    }

    public AbstractRedissonLockBuilder(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        defaultCallBackStrategy();
    }

    public Builder setRedissonClient(RedissonClient redissonClient) {
        getRealBuilder().redissonClient = redissonClient;
        return getRealBuilder();
    }

    /**
     * 设置锁等待时长 <=0 不等待
     *
     * @param time
     * @param timeUnit
     * @return
     */
    public Builder waitFor(long time, TimeUnit timeUnit) {
        getRealBuilder().lockExpire.setWaitTime(time > 0 ? time : getRealBuilder().lockExpire.getWaitTime());
        getRealBuilder().lockExpire.setTimeUnit(timeUnit != null ? timeUnit : getRealBuilder().lockExpire.getTimeUnit());
        return getRealBuilder();
    }

    /**
     * 设置锁等待时长 毫秒 <=0 不等待
     *
     * @param time
     * @return
     */
    public Builder waitFor(long time) {
        return getRealBuilder().waitFor(time, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置释放时间 必须要>0
     *
     * @param time
     * @return
     */
    public Builder releaseFor(long time) {
        getRealBuilder().lockExpire.setLeaseTime(time > 0 ? time : getRealBuilder().lockExpire.getLeaseTime());
        return getRealBuilder();
    }


    /**
     * 未获取到锁或者等待线程被打断{@link InterruptedException}
     *
     * @return
     */
    public Builder onLockFail(LockFailCallBack<LR> lockFailCallBack) {
        getRealBuilder().lockFailCallBackWrapper.setLockEventCallBack(lockFailCallBack);
        return getRealBuilder();
    }

    /**
     * 流程结束后对锁的处理
     *
     * @return
     */
    public Builder onFinally(LockFinalCallBack<LR> lockFinalCallBack) {
        getRealBuilder().lockFinalCallBackWrapper.setLockEventCallBack(lockFinalCallBack);
        return getRealBuilder();
    }

    public Builder onExecError(ExecErrorCallBack<LR> execErrorCallBack) {
        getRealBuilder().execErrorCallBackWrapper.setLockEventCallBack(execErrorCallBack);
        return getRealBuilder();
    }

    private Builder getRealBuilder() {
        return (Builder) this;
    }

    /**
     * 默认锁事件策略
     *
     * @return
     */
    protected void defaultCallBackStrategy() {
        this.lockSuccessCallBackWrapper = new LockEventCallBackWrapper<>();
        this.lockFailCallBackWrapper = new LockEventCallBackWrapper<>(new DoNothingCallBack<>());
        this.execErrorCallBackWrapper = new LockEventCallBackWrapper<>(new FastUnlockErrorCallBack<>());
        this.lockFinalCallBackWrapper = new LockEventCallBackWrapper<>(new FastUnlockFinalCallBack<>());
    }

    /**
     * 初始化一个加锁结果
     *
     * @return
     */
    protected abstract LR initialLockResult();


    /**
     * 执行
     *
     * @param lockSuccessCallBack
     * @return 执行成功后拿到预期返回值，否则为null
     */
    @Override
    public <Result> Result submit(Function<LR, Result> lockSuccessCallBack) {
        return exec(lockSuccessCallBack);
    }

    /**
     * 执行，不需要返回值
     *
     * @param lockSuccessCallBack
     * @return
     */
    @Override
    public void submitNr(Consumer<LR> lockSuccessCallBack) {
        exec(t -> {
            lockSuccessCallBack.accept(t);
            return null;
        });
    }

    /**
     * 完整的执行流程
     *
     * @param successFunc
     * @param <T>
     * @return
     */
    private <T> T exec(Function<LR, T> successFunc) {
        T t = null;
        RLock rLock = build();
        LR lockResult = initialLockResult();
        lockResult.setLock(rLock);
        try {
            boolean hasLock = false;
            //无等待锁，立即返回拿锁状态
            if (lockExpire.getWaitTime() == 0) {
                hasLock = rLock.tryLock();
            }
            //有等待锁，在有效时间内获取锁
            else if (lockExpire.getWaitTime() > 0) {
                //redisson方案优化，如果未制定释放时长，则使用watch dog，忽略默认释放时间
                hasLock = rLock.tryLock(lockExpire.getWaitTime(), Optional.ofNullable(lockExpire.getLeaseTime()).orElse(LockExpire.MAX_RELEASE_TIME), lockExpire.getTimeUnit());
            }
            //强制锁
            else {
                rLock.lock();
                hasLock = true;
            }
            lockResult.setHasLock(hasLock);
            if (hasLock) {
                try {
                    lockResult.setLockHolderId(Thread.currentThread().getId());
                    t = successFunc.apply(lockResult);
                } catch (Exception e) {
                    lockResult.setExecException(e);
                    execErrorCallBackWrapper.accept(lockResult);
                    throw e;
                }
            } else {
                lockFailCallBackWrapper.accept(lockResult);
            }
        } catch (InterruptedException e) {
            lockResult.setInterruptedException(e);
            lockFailCallBackWrapper.accept(lockResult);
        } finally {
            lockFinalCallBackWrapper.accept(lockResult);
        }
        return t;
    }

}
