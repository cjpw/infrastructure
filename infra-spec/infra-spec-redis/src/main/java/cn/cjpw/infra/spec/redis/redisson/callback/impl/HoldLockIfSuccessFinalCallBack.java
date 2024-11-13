package cn.cjpw.infra.spec.redis.redisson.callback.impl;

import cn.cjpw.infra.spec.redis.lock.callback.LockFinalCallBack;
import cn.cjpw.infra.spec.redis.lock.result.LockResult;
import org.redisson.api.RLock;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 锁持有者尝试对锁持有固定时长
 *
 * @author jun.chen1
 * @since 2022/10/27 13:37
 **/
public class HoldLockIfSuccessFinalCallBack<Result extends LockResult<RLock>> implements LockFinalCallBack<Result> {

    private static ScheduledExecutorService executorService;

    private Duration duration;


    static {
        executorService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, new CallerRunsPolicy());
    }

    public HoldLockIfSuccessFinalCallBack(Duration duration) {
        this.duration = duration;
    }


    @Override
    public void accept(Result lockResult) {
        if (lockResult.getExecException() != null || lockResult.getInterruptedException() != null) {
            if (lockResult.isHasLock()) {
                lockResult.getLock().unlock();
            }
        } else {
            if (lockResult.isHasLock()) {
                /* bug:使用redisson锁release机制，锁竞争者在手动代码修改锁释放时间之前开始竞争时，可能因为拿到还是之前的锁释放时间（大于当前修改后的时间），
                但锁超时失效后不会通知到锁竞争者，导致等待时间一直达到waitTime后，因为等待超时拿锁失败
                 */
                //lockResult.getRLock().lock(lockExpire.getWaitTime(), lockExpire.getTimeUnit());
                executorService.schedule(() -> {
//                    System.out.println("ssssssssssssssssssssssssss");
                    lockResult.getLock().unlockAsync(lockResult.getLockHolderId());
//                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                }, duration.toMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }
}
