package cn.cjpw.infra.spec.base.mock;

import java.lang.annotation.*;

/**
 * 模拟调用外部接口
 *
 * @author jun.chen1
 * @since 2022/12/5 10:04
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RemoteApiMock {

    /**
     * 替代的mock方法,要求同类下面同参数
     *
     * @return
     */
    String mockMethod();

    /**
     * 默认的接口延迟时间
     *
     * @return
     */
    long costTime() default 0L;

    /**
     * mock方法的名称，需要保证全局唯一。如果不填写，则使用mockClass#mockMethod作为名称
     *
     * @return
     */
    String mockName() default "";
}
