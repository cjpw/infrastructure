package cn.cjpw.infra.spec.redis.lock.callback;

/**
 * 成功/失败回调执行完成之后的收尾处理
 * <p>
 * 一定会触发
 *
 * @param <LR> 锁的结果
 * @author chenjun
 * @since 2019/7/23 15:28
 */
@FunctionalInterface
public interface LockFinalCallBack<LR> extends LockEventCallBack<LR> {

//    static FastUnlockFinalCallBack<LockResult<RLock>> fastUnlock() {
//        return new FastUnlockFinalCallBack<>();
//    }
//
//    static HoldLockIfSuccessFinalCallBack<LockResult<RLock>> holdLock(LockExpire lockExpire) {
//        return new HoldLockIfSuccessFinalCallBack<>(lockExpire);
//    }
}
