package cn.cjpw.infra.spec.base.exception.constant;

/**
 * 自定义异常相关常量
 *
 * @author jun.chen1
 * @since 2020/6/17 14:50
 **/
public class ExceptionConstant {
    /**
     * 内部应用id 无指定
     */
    public static final String SYS_ID_BASE = "";

    /**
     * 异常特性 无指定
     */
    public static final int FEATURE_NONE = -1;
    /**
     * 异常特性 是否可以重试,1bit,index=0
     */
    public static final int FEATURE_RETRY = 1;
    /**
     * 异常特性 是否特定异常,1bit,index=0
     */
    public static final int FEATURE_SPECIFIC = 2;


    /**
     * 异常问题大类 权限问题
     */
    public static final String EXCEPTION_TOP_TYPE_PERMISSION = "1";
    /**
     * 异常问题大类 校验问题
     */
    public static final String EXCEPTION_TOP_TYPE_VALIDATION = "2";
    /**
     * 异常问题大类 业务数据问题
     */
    public static final String EXCEPTION_TOP_TYPE_DATA = "3";
    /**
     * 异常问题大类 HTTP请求问题
     */
    public static final String EXCEPTION_TOP_TYPE_HTTP = "4";
    /**
     * 异常问题大类 远程接口问题
     */
    public static final String EXCEPTION_TOP_TYPE_REMOTE_API = "5";
    /**
     * 异常问题大类 数据库问题
     */
    public static final String EXCEPTION_TOP_TYPE_DB = "6";
    /**
     * 异常问题大类 中间件问题
     */
    public static final String EXCEPTION_TOP_MIDDLEWARE = "7";
    /**
     * 异常问题大类 系统内部问题
     */
    public static final String EXCEPTION_TOP_SYS = "9";

}
