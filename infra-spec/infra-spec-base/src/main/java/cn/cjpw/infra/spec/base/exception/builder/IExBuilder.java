package cn.cjpw.infra.spec.base.exception.builder;

/**
 * 异常信息构造工具接口声明
 * 1.用于无差别在内部各个application里面抛出异常信息
 * 2.提供简洁语法配置异常类，支撑异常架构设计，防止异常类构造逐渐复杂引起代码调整大
 *
 * @author jun.chen1
 * @since 2020/8/7 17:54
 **/
public interface IExBuilder<T extends RuntimeException> {

    /**
     * 返回被装饰的异常类
     *
     * @return
     */
    T build();

    /**
     * 构造完异常类直接抛出
     */
    default void buildAndThrow() {
        throw build();
    }

}
