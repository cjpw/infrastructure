package cn.cjpw.infra.spec.base.exception.outter.db;

import cn.cjpw.infra.spec.base.exception.ExceptionTypeMatcher;
import cn.cjpw.infra.spec.base.exception.enums.IError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.TransactionException;

/**
 * 由spring dao 抛出的异常识别转化
 * <p>
 * 基于spring 5.2.3
 *
 * @author jun.chen1
 * @since 2021/8/23 17:24
 **/
public class SpringDbExceptionMatcher implements ExceptionTypeMatcher {

    @Override
    public IError getIErrorResult(Throwable throwable) {
        if (throwable instanceof DataIntegrityViolationException) {
            return IError.DB_DATA_INTEGRITY_VIOLATION;
        } else if (throwable instanceof UncategorizedSQLException || throwable instanceof BadSqlGrammarException) {
            return IError.DB_SQL_ERROR;
        }
        return IError.DB_ERROR;
    }

    @Override
    public boolean test(Throwable throwable) {
        if (throwable instanceof TransactionException) {
            return true;
        } else if (throwable instanceof TransientDataAccessException) {
            return true;
        } else if (throwable instanceof NonTransientDataAccessException) {
            String exClassName = throwable.getClass().getName();
            return exClassName.startsWith("org.springframework.dao") || exClassName.startsWith("org.springframework.jca")
                    || exClassName.startsWith("org.springframework.jdbc");

        }
        return false;
    }
}
