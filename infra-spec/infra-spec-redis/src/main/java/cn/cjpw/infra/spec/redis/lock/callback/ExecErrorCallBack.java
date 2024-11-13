package cn.cjpw.infra.spec.redis.lock.callback;

/**
 * 执行报错的处理策略
 * <p>
 * 该流程不会影响异常的抛出，只做增强
 *
 * @param <LR> 锁的结果
 * @author jun.chen1
 * @since 2022/10/20 18:49
 **/
@FunctionalInterface
public interface ExecErrorCallBack<LR> extends LockEventCallBack<LR> {

//    static FastUnlockErrorCallBack<LockResult<RLock>> fastUnlock() {
//        return new FastUnlockErrorCallBack<>();
//    }


}
