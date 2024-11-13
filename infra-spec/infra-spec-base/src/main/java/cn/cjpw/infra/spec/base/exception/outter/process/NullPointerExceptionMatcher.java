package cn.cjpw.infra.spec.base.exception.outter.process;

import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;
import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * @author jun.chen1
 * @since 2021/8/30 16:39
 **/
public class NullPointerExceptionMatcher implements ExceptionTypeMatcher {
    @Override
    public IError getIErrorResult(Throwable throwable) {
        return IError.SYS_UN_HANDLED_NULL_VALUE;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof NullPointerException) {
            return true;
        }
        return false;
    }
}
