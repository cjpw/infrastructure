package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * 专用于带事务的接口抛出异常，但不应影响事务提交
 * eg. @Transactional(noRollbackFor = TaskNoRollBackException.class)
 *
 * @author jun.chen1
 * @since 2020/7/16 13:55
 **/
public class WrappedNoRollBackException extends WrappedException {
    public WrappedNoRollBackException(IError errorInfo) {
        super(errorInfo);
    }

    public WrappedNoRollBackException(String detailMsg, IError errorInfo) {
        super(detailMsg, errorInfo);
    }

    public WrappedNoRollBackException(Throwable cause, IError errorInfo) {
        super(cause, errorInfo);
    }

    public WrappedNoRollBackException(IError errorInfo, String showMsg) {
        super(errorInfo, showMsg);
    }

    public WrappedNoRollBackException(String detailMsg, IError errorInfo, String showMsg) {
        super(detailMsg, errorInfo, showMsg);
    }

//    public TaskNoRollBackException(IError errorInfo, String showMsg, boolean appendMsg) {
//        super(errorInfo, showMsg, appendMsg);
//    }

    public WrappedNoRollBackException(Throwable cause, IError errorInfo, String showMsg) {
        super(cause, errorInfo, showMsg);
    }
}
