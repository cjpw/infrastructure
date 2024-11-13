package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * 项目异常基类
 *
 * @author jun.chen1
 * @since 2022/4/13 17:05
 **/
public abstract class WrappedException extends RuntimeException implements WrappedThrowable {

    /**
     * 自定义状态类
     */
    private IError errorResult;

    /**
     * 定制错误描述，客户端可见
     */
    private String showMsg;

    protected WrappedException(IError errorInfo) {
        super(errorInfo.getErrorDesc());
        this.initial(errorInfo);
    }

    protected WrappedException(String detailMsg, IError errorInfo) {
        super(detailMsg);
        this.initial(errorInfo);
    }

    protected WrappedException(Throwable cause, IError errorInfo) {
        super(errorInfo.getErrorDesc(), cause);
        this.initial(errorInfo);
    }

    protected WrappedException(IError errorInfo, String showMsg) {
        super(showMsg);
        this.initial(errorInfo);
        this.showMsg = showMsg;
    }

    protected WrappedException(String detailMsg, IError errorInfo, String showMsg) {
        super(detailMsg);
        this.initial(errorInfo);
        this.showMsg = showMsg;
    }

    protected WrappedException(Throwable cause, IError errorInfo, String showMsg) {
        super(showMsg, cause);
        this.initial(errorInfo);
        this.showMsg = showMsg;
    }

    private void initial(IError errorResult) {
        this.errorResult = errorResult;
        this.showMsg = errorResult.getErrorDesc();
    }

    /**
     * 获取 自定义状态类
     *
     * @return errorResult 自定义状态类
     */
    @Override
    public IError getErrorResult() {
        return this.errorResult;
    }

    /**
     * 获取 定制错误描述，客户端可见
     *
     * @return showMsg 定制错误描述，客户端可见
     */
    @Override
    public String getShowMsg() {
        return this.showMsg;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName()).append(": ");
        stringBuilder.append("[").append(getErrorResult().getErrorCode()).append("-").append(getErrorResult().getErrorDesc())
            .append("] ");
        stringBuilder.append(getShowMsg());
        return stringBuilder.toString();
    }
}
