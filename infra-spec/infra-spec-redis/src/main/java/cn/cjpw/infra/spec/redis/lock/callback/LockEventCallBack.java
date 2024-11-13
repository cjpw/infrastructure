package cn.cjpw.infra.spec.redis.lock.callback;

/**
 * 锁使用相关事件声明
 *
 * @author jun.chen1
 * @since 2023/4/23 10:57
 **/
public interface LockEventCallBack<T> {

    void accept(T t);
}
