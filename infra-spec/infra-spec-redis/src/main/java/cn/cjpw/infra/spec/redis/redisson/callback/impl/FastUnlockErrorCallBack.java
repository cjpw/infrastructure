package cn.cjpw.infra.spec.redis.redisson.callback.impl;

import cn.cjpw.infra.spec.redis.lock.callback.ExecErrorCallBack;
import cn.cjpw.infra.spec.redis.lock.result.LockResult;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

/**
 * 尽快释放锁,不做其他事情
 *
 * @author jun.chen1
 * @since 2022/10/20 18:51
 **/
@Slf4j
public class FastUnlockErrorCallBack<Result extends LockResult<RLock>> implements ExecErrorCallBack<Result> {

    @Override
    public void accept(Result lockResult) {
        if (lockResult.isHasLock()) {
            try {
                lockResult.getLock().unlock();
            } catch (Exception e) {
                log.warn("unlock fail when executing throws error",e);
            }
            //同时清理锁持有信息
            lockResult.setHasLock(false);
            lockResult.setLockHolderId(-1L);
        }
    }
}
