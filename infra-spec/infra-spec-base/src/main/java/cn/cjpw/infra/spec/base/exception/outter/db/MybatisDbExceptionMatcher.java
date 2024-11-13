package cn.cjpw.infra.spec.base.exception.outter.db;

import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;
import cn.cjpw.infra.spec.base.exception.enums.IError;
import org.apache.ibatis.exceptions.PersistenceException;

/**
 * 由mybatis 抛出的异常识别转化
 * <p>
 * 基于mybatis 3.5.2
 *
 * @author jun.chen1
 * @since 2021/8/24 14:22
 **/
public class MybatisDbExceptionMatcher implements ExceptionTypeMatcher {

    @Override
    public IError getIErrorResult(Throwable throwable) {
        return IError.DB_ERROR;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof PersistenceException) {
            return true;
        }
        return false;
    }
}
