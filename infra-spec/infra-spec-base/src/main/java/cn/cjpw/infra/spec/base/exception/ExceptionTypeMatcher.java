package cn.cjpw.infra.spec.base.exception;

import cn.cjpw.infra.spec.base.exception.enums.IError;
import cn.cjpw.infra.spec.base.exception.outter.data.OutOfRangeExceptionMatcher;
import cn.cjpw.infra.spec.base.exception.outter.db.JdbcDbExceptionMatcher;
import cn.cjpw.infra.spec.base.exception.outter.db.MybatisDbExceptionMatcher;
import cn.cjpw.infra.spec.base.exception.outter.db.SpringDbExceptionMatcher;
import cn.cjpw.infra.spec.base.exception.outter.process.NullPointerExceptionMatcher;

import java.util.function.Predicate;

/**
 * 根据捕获的外部异常，自动转化为内部异常码
 *
 * @author jun.chen1
 * @since 2021/8/24 15:28
 **/
public interface ExceptionTypeMatcher extends Predicate<Throwable> {

    IError getIErrorResult(Throwable throwable);

    /* data start */
    ExceptionTypeMatcher OOR_DATA_MATCHER = new OutOfRangeExceptionMatcher();
    /* data end */
    /* db start */
    ExceptionTypeMatcher JDBC_DB_MATCHER = new JdbcDbExceptionMatcher();
    ExceptionTypeMatcher MYBATIS_DB_MATCHER = new MybatisDbExceptionMatcher();
    ExceptionTypeMatcher SPRING_DB_MATCHER = new SpringDbExceptionMatcher();
    /* db end */
    /* sys start */
    ExceptionTypeMatcher NPE_DATA_MATCHER = new NullPointerExceptionMatcher();
    /* sys end */
}