package cn.cjpw.infra.spec.base.exception.builder;

import cn.cjpw.infra.spec.base.exception.WrappedModuleException;
import cn.cjpw.infra.spec.base.exception.enums.IError;
import cn.cjpw.infra.spec.base.exception.outter.OuterExceptionManager;
import cn.cjpw.infra.spec.base.exception.util.ExceptionCatcher;
import cn.hutool.core.util.StrUtil;

/**
 * 标准化的exception构造器，提供给不同application透明的api
 *
 * @author jun.chen1
 * @since 2020/8/17 19:29
 **/
public class ExBuilder extends AbstractExBuilder<WrappedModuleException, IError, ExBuilder> {

    private ExBuilder() {
    }

    /**
     * 非自定义异常包装，根据策略配置转化成指定异常码，自动添加cause
     *
     * @param throwable 原始外部异常
     * @return
     */
    public static ExBuilder wrapUnknown(Throwable throwable) {
        IError iError = OuterExceptionManager.instance().convert(throwable);
        return of(iError).withCause(throwable);
    }

    /**
     * 非自定义异常包装，根据策略配置转化成指定异常码，自动添加cause
     *
     * @param throwable   原始外部异常
     * @param traceCodeId 结合调用链id，对异常提示信息进行简单的美化
     * @return
     */
    public static ExBuilder wrapUnknown(Throwable throwable, String traceCodeId) {
        IError iError = OuterExceptionManager.instance().convert(throwable);
        ExBuilder exBuilder = of(iError).withCause(throwable);
        if (iError.standardError()) {
            String msg = ExceptionCatcher.beautifyErrorMsg(iError, traceCodeId);
            exBuilder.withShowMsg(msg);
        } else {
            exBuilder.withShowMsg(iError.getErrorDesc());
        }
        return exBuilder;
    }

    /**
     * 标准异常包装
     *
     * @return
     */
    public static ExBuilder of(IError errorResult) {
        ExBuilder defaultExBuilder = new ExBuilder();
        defaultExBuilder.initErrorInfo(errorResult);
        return defaultExBuilder;
    }

    @Override
    public WrappedModuleException build() {
        WrappedModuleException exception;
        if (StrUtil.isNotBlank(detailMsg)) {
            if (StrUtil.isNotBlank(showMsg)) {
                exception = new WrappedModuleException(detailMsg, this.errorResult, this.showMsg.toString());
            } else {
                exception = new WrappedModuleException(detailMsg, this.errorResult);
            }
        } else if (StrUtil.isNotBlank(showMsg)) {
            exception = new WrappedModuleException(this.errorResult, this.showMsg.toString());
        } else {
            exception = new WrappedModuleException(this.errorResult);
        }

        if (this.cause != null) {
            exception.initCause(this.cause);
        }
        return exception;
    }


}
