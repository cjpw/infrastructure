package cn.cjpw.infra.spec.redis.lock.callback;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author jun.chen1
 * @since 2023/6/26 16:22
 **/
public class LockEventCallBackWrapper<LR> implements LockEventCallBack<LR> {
    private LockEventCallBack<LR> lockEventCallBack;
    private Consumer<LR> beforeEventConsumer;
    private Consumer<LR> afterEventConsumer;

    public LockEventCallBackWrapper() {
    }

    public LockEventCallBackWrapper(LockEventCallBack<LR> lockEventCallBack) {
        this.lockEventCallBack = lockEventCallBack;
    }

    protected void beforeEvent(LR lr) {
        Optional.ofNullable(beforeEventConsumer).ifPresent(item -> item.accept(lr));
    }

    protected void afterEvent(LR lr) {
        Optional.ofNullable(afterEventConsumer).ifPresent(item -> item.accept(lr));
    }

    @Override
    public void accept(LR lr) {
        beforeEvent(lr);
        Optional.ofNullable(lockEventCallBack).ifPresent(lockEventCallBack -> lockEventCallBack.accept(lr));
        afterEvent(lr);
    }

    public LockEventCallBack<LR> getLockEventCallBack() {
        return lockEventCallBack;
    }

    public void setLockEventCallBack(LockEventCallBack<LR> lockEventCallBack) {
        this.lockEventCallBack = lockEventCallBack;
    }

    public Consumer<LR> getBeforeEventConsumer() {
        return beforeEventConsumer;
    }

    public void setBeforeEventConsumer(Consumer<LR> beforeEventConsumer) {
        this.beforeEventConsumer = beforeEventConsumer;
    }

    public Consumer<LR> getAfterEventConsumer() {
        return afterEventConsumer;
    }

    public void setAfterEventConsumer(Consumer<LR> afterEventConsumer) {
        this.afterEventConsumer = afterEventConsumer;
    }
}
