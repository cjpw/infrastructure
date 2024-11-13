package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * 专门用于远程接口调用，但接口本身只返回含错误码的对象而不是抛出异常的情况
 *
 * @author jun.chen1
 * @since 2020/6/17 20:44
 **/
public class WrappedRemoteApiException extends WrappedException {

    /**
     * @param errorCode 服务方返回的错误码
     * @param showMsg   服务方返回的异常提示文本
     */
    public WrappedRemoteApiException(String errorCode, String showMsg) {
        super(new ErrorResultWrapper(errorCode, showMsg));
    }

    /**
     * @param errorCode          服务方返回的错误码
     * @param showMsg            服务方返回的异常提示文本
     * @param exceptionDetailMsg exception中包装的detailMsg
     */
    public WrappedRemoteApiException(String errorCode, String showMsg, String exceptionDetailMsg) {
        super(exceptionDetailMsg, new ErrorResultWrapper(errorCode, showMsg));
    }

    static class ErrorResultWrapper implements IError {
        private final String errorCode;
        private final String errorMessage;

        public ErrorResultWrapper(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        @Override
        public String getErrorCode() {
            return errorCode;
        }

        @Override
        public String getErrorDesc() {
            return errorMessage;
        }

    }
}
