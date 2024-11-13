package cn.cjpw.infra.spec.base.exception.util;

import cn.cjpw.infra.spec.base.exception.ModuleExceptionRegistry;
import cn.cjpw.infra.spec.base.exception.WrappedException;
import cn.cjpw.infra.spec.base.exception.constant.ExceptionConstant;
import cn.cjpw.infra.spec.base.exception.enums.IError;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 自定义异常处理工具
 *
 * @author jun.chen1
 * @date 2020/5/29
 */
public class ExceptionCatcher {
    /**
     * 外部接口问题美化提示模板
     */
    public static String BEAUTIFY_MSG_TEMPLATE_REMOTE_API = "三方服务繁忙，请联系IT或稍后重试，IT协助码[%s]";
    /**
     * 数据库问题美化提示模板
     */
    public static String BEAUTIFY_MSG_TEMPLATE_DATABASE = "数据服务繁忙，请联系IT或稍后重试，IT协助码[%s]";
    /**
     * 组件问题美化提示模板
     */
    public static String BEAUTIFY_MSG_TEMPLATE_MIDDLEWARE = "组件服务繁忙，请联系IT或稍后重试，IT协助码[%s]";
    /**
     * 系统问题美化提示模板
     */
    public static String BEAUTIFY_MSG_TEMPLATE_SYSTEM = "系统繁忙，请联系IT或稍后重试，IT协助码[%s]";

    /**
     * 获取一个用于定位异常日志的id，能够保证低冲突率就行
     *
     * @return
     */
    public static String errorTraceCodeGen() {
        String uuid = UUID.randomUUID().toString();
        String traceId = String.valueOf(Math.abs(uuid.hashCode()));
        return traceId;
    }

    /**
     * 判断该异常是否是建议重试的异常
     *
     * @param e
     * @return
     */
    public static boolean canRetry(WrappedException e) {
        Long featureNumber = e.getErrorResult().getFeatureCode();
        return (featureNumber & ExceptionConstant.FEATURE_RETRY) == 1;
    }

    /**
     * 判断该异常是否是特定场景异常
     *
     * @param e
     * @return
     */
    public static boolean specific(WrappedException e) {
        Long featureNumber = e.getErrorResult().getFeatureCode();
        return (featureNumber & ExceptionConstant.FEATURE_SPECIFIC) == 1;
    }

    /**
     * 美化用户提示文案
     * 如果不需要美化则返回外部指定的{@link WrappedException#getShowMsg()}
     * //TODO 临时的，粗粒度的，解决方案，待异常架构完善后优化
     *
     * @param iError
     * @return
     */
    public static String beautifyErrorMsg(IError iError, String traceCodeId) {
        String beauty = "";
        if (isRemoteApiError(iError)) {
            if (!IError.REMOTE_API_UNSUCCESSFUL_RESULT.getCodeSubType().equals(iError.getCodeSubType())) {
                beauty = String.format(BEAUTIFY_MSG_TEMPLATE_REMOTE_API, traceCodeId);
            }
        } else if (isDataBaseError(iError)) {
            beauty = String.format(BEAUTIFY_MSG_TEMPLATE_DATABASE, traceCodeId);
        } else if (isMiddlewareError(iError)) {
            beauty = String.format(BEAUTIFY_MSG_TEMPLATE_MIDDLEWARE, traceCodeId);
        } else if (isSystemError(iError)) {
            beauty = String.format(BEAUTIFY_MSG_TEMPLATE_SYSTEM, traceCodeId);
        }
        return beauty;
    }

    /**
     * 判断是否是对用户美化过的异常提示
     *
     * @param message
     * @return
     */
    public static boolean isBeautifyMessage(String message) {
        return message.contains("请联系IT或稍后重试");
    }

    //    /**
    //     * 判断是否权限问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isPermissionError(TaskException taskException) {
    //        return isPermissionError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否权限问题
     *
     * @param iError
     * @return
     */
    public static boolean isPermissionError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_PERMISSION.equals(codeType);
    }

    //    /**
    //     * 判断是否校验问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isValidationError(TaskException taskException) {
    //        return isPermissionError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否校验问题
     *
     * @param iError
     * @return
     */
    public static boolean isValidationError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_VALIDATION.equals(codeType);
    }

    //    /**
    //     * 判断是否数据问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isDataError(TaskException taskException) {
    //        return isDataError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否数据问题
     *
     * @param iError
     * @return
     */
    public static boolean isDataError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_DATA.equals(codeType);
    }

    //    /**
    //     * 判断是否是http请求问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isHttpError(TaskException taskException) {
    //        return isHttpError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否是http请求问题
     *
     * @param iError
     * @return
     */
    public static boolean isHttpError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_HTTP.equals(codeType);
    }

    //    /**
    //     * 判断是否是调用外部平台接口问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isRemoteApiError(TaskException taskException) {
    //        return isRemoteApiError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否是调用外部平台接口问题
     *
     * @param iError
     * @return
     */
    public static boolean isRemoteApiError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_REMOTE_API.equals(codeType);
    }

    //    /**
    //     * 判断是否是db问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isDataBaseError(TaskException taskException) {
    //        return isDataBaseError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否是db问题
     *
     * @param iError
     * @return
     */
    public static boolean isDataBaseError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_TYPE_DB.equals(codeType);
    }

    //    /**
    //     * 判断是否是中间件问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isMiddlewareError(TaskException taskException) {
    //        return isMiddlewareError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否是中间件问题
     *
     * @param iError
     * @return
     */
    public static boolean isMiddlewareError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_MIDDLEWARE.equals(codeType);
    }


    //    /**
    //     * 判断是否是系统内部问题
    //     *
    //     * @param taskException
    //     * @return
    //     */
    //    public static boolean isSystemError(TaskException taskException) {
    //        return isSystemError(taskException.getErrorResult());
    //    }

    /**
     * 判断是否是系统内部问题
     *
     * @param iError
     * @return
     */
    public static boolean isSystemError(IError iError) {
        String codeType = iError.getCodeType();
        return ExceptionConstant.EXCEPTION_TOP_SYS.equals(codeType);
    }

    /**
     * 获取异常链源头
     *
     * @param taskException
     * @return
     */
    public static Throwable findOriginalError(WrappedException taskException) {
        Throwable source = taskException;
        while (source.getCause() != null) {
            source = source.getCause();
        }
        return source;
    }

    /**
     * 获取整个异常类捕获链
     *
     * @param taskException
     * @return
     */
    public static List<Throwable> findErrorLink(WrappedException taskException) {
        List<Throwable> list = new LinkedList<>();
        Throwable source = taskException;
        list.add(source);
        do {
            list.add(source.getCause());
            source = source.getCause();
        }
        while (source.getCause() != null);

        return list;
    }

    /**
     * 是否异常来源于当前应用内
     *
     * @param taskException
     * @return
     */
    public static boolean throwByCurrentApplication(WrappedException taskException) {
        return ModuleExceptionRegistry.instance().getModuleId().equals(taskException.getErrorResult().getModuleId());
    }

}
