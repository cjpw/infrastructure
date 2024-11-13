package cn.cjpw.infra.spec.base.infra.spec.spring.transcation;

import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * 事务合并的桥接工具，实现本地多个不同带有事务注解的service快速集成到一个事务里面，不用再添加新的service或接口
 *
 * @deprecated 如下,spring已集成
 * @see org.springframework.transaction.support.TransactionTemplate
 * @author jun.chen1
 * @since 2021/9/7 15:06
 **/
@Deprecated
public class TransactionalMergeBridge {

    /**
     * 始终通用的事务传播方式组合service调用
     * {@link org.springframework.transaction.annotation.Propagation}
     *
     * @param supplier
     */
    @Transactional(rollbackFor = Exception.class)
    public <T> T merge(Supplier<T> supplier) {
        return supplier.get();
    }

}
