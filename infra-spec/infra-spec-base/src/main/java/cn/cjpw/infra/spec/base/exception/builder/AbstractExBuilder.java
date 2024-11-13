package cn.cjpw.infra.spec.base.exception.builder;

import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * 异常类包装器
 * 1.用于无差别在内部各个application里面抛出异常信息
 * 2.提供简洁语法配置异常类，增强一些功能，支撑异常架构设计
 *
 * @author jun.chen1
 * @since 2020/8/6 14:51
 **/
public abstract class AbstractExBuilder<T extends RuntimeException, R extends IError, S extends IExBuilder<T>> implements IExBuilder<T> {
    /**
     * exception cause by
     */
    protected Throwable cause;
    /**
     * 自定义状态类
     */
    protected R errorResult;
    /**
     * 定制错误描述，客户端可见
     */
    protected String showMsg;
    /**
     * Throwable 打印的detailMessage
     */
    protected String detailMsg;

    /**
     * 实例
     *
     * @return
     */
    protected S getInstance() {
        return (S) this;
    }

    /**
     * 异常码相关
     *
     * @param errorResult
     * @return
     */
    protected S initErrorInfo(R errorResult) {
        this.errorResult = errorResult;
        return getInstance();
    }

    /**
     * 填充cause by
     *
     * @param cause
     * @return
     */
    public S withCause(Throwable cause) {
        this.cause = cause;
        return getInstance();
    }

    /**
     * Throwable 打印的detailMessage
     *
     * @param detailMsg
     * @return
     */
    public S withDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
        return getInstance();
    }

    /**
     * 追加返回客户端异常消息
     *
     * @param showMsg
     * @return
     */
    public S withShowMsg(String showMsg) {
        this.showMsg = showMsg;
        return getInstance();
    }


}
