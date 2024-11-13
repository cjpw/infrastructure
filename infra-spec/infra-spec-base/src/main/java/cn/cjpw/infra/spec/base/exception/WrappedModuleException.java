package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;
import cn.hutool.core.util.StrUtil;

/**
 * 项目异常通用包装类
 *
 * @author jun.chen1
 * @since 2022/4/13 17:06
 **/
public class WrappedModuleException extends WrappedException {


    public WrappedModuleException(IError errorInfo) {
        super(new ErrorResultWrapper(errorInfo));
    }

    public WrappedModuleException(Throwable cause, IError errorInfo) {
        super(cause, new ErrorResultWrapper(errorInfo));
    }

    public WrappedModuleException(IError errorInfo, String showMsg) {
        super(new ErrorResultWrapper(errorInfo), showMsg);
    }

    public WrappedModuleException(Throwable cause, IError errorInfo, String showMsg) {
        super(cause, new ErrorResultWrapper(errorInfo), showMsg);
    }

    public WrappedModuleException(String detailMsg, IError errorInfo) {
        super(detailMsg, new ErrorResultWrapper(errorInfo));
    }

    public WrappedModuleException(String detailMsg, IError errorInfo, String showMsg) {
        super(detailMsg, new ErrorResultWrapper(errorInfo), showMsg);
    }

    @Override
    public synchronized WrappedModuleException initCause(Throwable cause) {
        return (WrappedModuleException) super.initCause(cause);
    }

    static class ErrorResultWrapper implements IError {
        private final String moduleId;

        private final IError iError;

        public ErrorResultWrapper(IError errorResult) {
            this.moduleId = StrUtil.blankToDefault(errorResult.getModuleId(),
                    ModuleExceptionRegistry.instance().getModuleId());
            this.iError = errorResult;
        }

        @Override
        public String getErrorCode() {
            return iError.getErrorCode();
        }

        @Override
        public String getErrorDesc() {
            return iError.getErrorDesc();
        }

        @Override
        public String getModuleId() {
            return moduleId;
        }

        @Override
        public Long getFeatureCode() {
            return iError.getFeatureCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (this == o || iError.equals(o)) {
                return true;
            }
            return false;
        }

    }
}
