package cn.cjpw.infra.spec.redis.redisson.callback.impl;

import cn.cjpw.infra.spec.redis.lock.callback.LockFinalCallBack;
import cn.cjpw.infra.spec.redis.lock.result.LockResult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

/**
 * 尽快释放锁,不做其他事情
 *
 * @author chenjun
 * @since 2019/7/23 16:42
 */
@Slf4j
public class FastUnlockFinalCallBack<Result extends LockResult<RLock>> implements LockFinalCallBack<Result> {


    @Override
    public void accept(Result lockResult) {
        if (lockResult.isHasLock()) {
            try {
                lockResult.getLock().unlock();
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("not locked")) {
                    log.info("already unlock,lockName={},detail={}", lockResult.getLock().getName(), e.getMessage());
                } else {
                    log.warn("unlock fail on finally,lockName={}", lockResult.getLock().getName(), e);
                }
            } catch (Exception e) {
                log.warn("unlock fail on finally", e);
            }
        }
    }

}
