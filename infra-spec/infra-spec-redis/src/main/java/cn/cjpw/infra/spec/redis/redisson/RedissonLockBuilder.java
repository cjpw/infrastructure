package cn.cjpw.infra.spec.redis.redisson;

import cn.cjpw.infra.spec.redis.lock.LockBuilder;
import org.redisson.api.RLock;

/**
 * @author jun.chen1
 * @since 2023/6/26 15:37
 **/
public interface RedissonLockBuilder<LR, Builder> extends LockBuilder<RLock, LR, Builder> {
}
