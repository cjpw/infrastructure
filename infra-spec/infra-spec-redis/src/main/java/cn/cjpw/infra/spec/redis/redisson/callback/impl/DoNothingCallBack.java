package cn.cjpw.infra.spec.redis.redisson.callback.impl;

import cn.cjpw.infra.spec.redis.lock.callback.LockFailCallBack;
import cn.cjpw.infra.spec.redis.lock.result.LockResult;
import org.redisson.api.RLock;

/**
 * 获取锁失败时，不做任何事情
 *
 * @author chenjun
 * @since 2019/7/23 17:09
 */
public class DoNothingCallBack<Result extends LockResult<RLock>> implements LockFailCallBack<Result> {

    @Override
    public void accept(Result result) {

    }
}
