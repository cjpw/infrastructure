package cn.cjpw.infra.spec.redis.lock;

import cn.cjpw.infra.spec.redis.lock.callback.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 分布式锁构造器
 *
 * @param <Builder> 构造器实例类型
 * @param <LR>      加锁内容类型
 * @param <Lock>    锁实例类型
 * @author jun.chen1
 * @since 2020/8/25 14:47
 **/
public interface LockBuilder<Lock, LR, Builder> {

    /**
     * 根据加锁策略创建一个未加锁实例
     *
     * @return
     */
    Lock build();

    /**
     * 设置锁等待时长 <=0 不等待
     *
     * @param time
     * @param timeUnit
     * @return
     */
    Builder waitFor(long time, TimeUnit timeUnit);

    /**
     * 设置锁等待时长 毫秒 <=0 不等待
     *
     * @param time
     * @return
     */
    Builder waitFor(long time);

    /**
     * 设置释放时间 必须要>0
     *
     * @param time
     * @return
     */
    Builder releaseFor(long time);

    /**
     * 未获取到锁或者等待线程被打断{@link InterruptedException}
     *
     * @return
     */
    Builder onLockFail(LockFailCallBack<LR> lockFailCallBack);

    /**
     * 流程结束后对锁的处理
     *
     * @return
     */
    Builder onFinally(LockFinalCallBack<LR> lockFinalCallBack);

    /**
     * 加锁成功后，执行submit中的业务流程报错的处理
     *
     * @param execErrorCallBack
     * @return
     */
    Builder onExecError(ExecErrorCallBack<LR> execErrorCallBack);

    /**
     * 执行
     *
     * @param lockSuccessCallBack
     * @param <Result>
     * @return 执行成功后拿到预期返回值，否则为null
     */
    <Result> Result submit(Function<LR, Result> lockSuccessCallBack);


    /**
     * 执行，不需要返回值
     *
     * @param lockSuccessCallBack
     * @return
     */
    void submitNr(Consumer<LR> lockSuccessCallBack);
}
