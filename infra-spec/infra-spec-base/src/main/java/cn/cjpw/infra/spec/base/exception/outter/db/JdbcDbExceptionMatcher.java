package cn.cjpw.infra.spec.base.exception.outter.db;

import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;
import cn.cjpw.infra.spec.base.exception.enums.IError;

import java.sql.SQLException;

/**
 * 由jdbc及上层应用抛出的异常识别转化
 *
 * @author jun.chen1
 * @since 2021/8/24 14:22
 **/
public class JdbcDbExceptionMatcher implements ExceptionTypeMatcher {

    @Override
    public IError getIErrorResult(Throwable throwable) {
        return IError.DB_ERROR;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof SQLException) {
            return true;
        }
        return false;
    }
}
