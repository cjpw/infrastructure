package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * 异常包装扩展能力
 * @author jun.chen1
 * @since 2022/4/13 17:08
 **/
public interface WrappedThrowable {

    /**
     * 获取 自定义状态类
     *
     * @return errorResult 自定义状态类
     */
    IError getErrorResult();

    /**
     * 获取 定制错误描述，客户端可见
     *
     * @return showMsg 定制错误描述，客户端可见
     */
    String getShowMsg();

}
