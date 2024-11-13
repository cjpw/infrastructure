package cn.cjpw.infra.spec.base.exception.outter.data;

import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;
import cn.cjpw.infra.spec.base.exception.enums.IError;

/**
 * @author jun.chen1
 * @since 2021/8/27 17:06
 **/
public class OutOfRangeExceptionMatcher implements ExceptionTypeMatcher {

    @Override
    public IError getIErrorResult(Throwable throwable) {
        return IError.DATA_ASSERT_COUNT_MATCH_FAILURE;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof IndexOutOfBoundsException) {
            return true;
        }
        return false;
    }
}
