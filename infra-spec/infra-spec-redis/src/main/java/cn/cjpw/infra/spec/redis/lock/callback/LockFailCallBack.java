package cn.cjpw.infra.spec.redis.lock.callback;

/**
 * 获取锁失败的回调,超时或者被中断
 *
 * @param <LR> 锁的结果
 * @author chenjun
 * @since 2019/7/23 15:28
 */
@FunctionalInterface
public interface LockFailCallBack<LR> extends LockEventCallBack<LR> {

//    static DoNothingFailCallBack<LockResult<RLock>> notToDoAny() {
//        return new DoNothingFailCallBack<>();
//    }

//    static LogFailCallBack logFailInfo(Logger logger) {
//        return new LogFailCallBack(logger);
//    }

}
