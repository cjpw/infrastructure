package cn.cjpw.infra.spec.redis.lock.callback;

/**
 * 成功获取锁的时候回调
 *
 * @param <LR> 锁的结果
 * @author chenjun
 * @since 2019/7/23 15:28
 */
@FunctionalInterface
public interface LockSuccessCallBack<LR> extends LockEventCallBack<LR> {

}
