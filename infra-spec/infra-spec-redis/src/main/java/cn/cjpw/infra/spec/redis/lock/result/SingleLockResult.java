package cn.cjpw.infra.spec.redis.lock.result;

import cn.cjpw.infra.spec.redis.lock.extention.HolderAdditional;

import java.util.function.Function;

/**
 * 普通的单个锁对象加锁结果信息扩展
 *
 * @author jun.chen1
 * @since 2023/6/26 11:17
 **/
public class SingleLockResult<T> extends LockResult<T> {

    /**
     * 锁名称
     */
    private final String lockName;

    private final Function<String, HolderAdditional> holderAdditionalFunction;

    public SingleLockResult(String lockName, Function<String, HolderAdditional> holderAdditionalFunction) {
        this.lockName = lockName;
        this.holderAdditionalFunction = holderAdditionalFunction;
    }

    /**
     * 获取 锁持有者添加的扩展描述
     *
     * @return holderAdditionalFunction 锁持有者添加的扩展描述
     */
    public HolderAdditional getHolderAdditional() {
        return holderAdditionalFunction.apply(lockName);
    }

    /**
     * 获取 锁名称
     *
     * @return lockName 锁名称
     */
    public String getLockName() {
        return this.lockName;
    }


}
