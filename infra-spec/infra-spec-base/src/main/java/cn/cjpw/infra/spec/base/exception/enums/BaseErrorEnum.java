package cn.cjpw.infra.spec.base.exception.enums;

import cn.cjpw.infra.spec.base.exception.constant.ExceptionConstant;

/**
 * 基础预定义错误枚举，描述了所有非业务情况的基础异常码
 * <p>
 * 各模块参考此枚举进行复用或者扩展
 *
 * @author jun.chen1
 * @since 2020/6/18 10:27
 **/
enum BaseErrorEnum implements IError {

    /* 权限问题 start */
    E_1000("1000", "权限异常"),
    E_1001("1001", "无接口调用权限"),
    E_1002("1002", "无数据操作权限"),
    E_1003("1003", "权限信息获取失败"),
    E_1004("1004", "账户状态异常"),
    E_1005("1005", "登录状态异常"),
    E_1006("1006", "授权信息已过时"),
    /* 权限问题 end */

    /* 校验问题 start */
    E_2000("2000", "参数校验不通过"),
    E_2001("2001", "入参基础校验不通过"),
    E_2002("2002", "参数解析、格式化失败"),
    E_2003("2003", "参数规则处理后不满足后续执行条件"),
    E_2004("2004", "已过期的请求"),
    E_2005("2005", "其他业务已锁定该资源"),
    E_2006("2006", "不再兼容的参数值"),
    E_2007("2007", "不再兼容的接口调用"),
    E_2008("2008", "重复请求"),
    /* 校验问题 end */

    /* 数据问题 start */
    E_3000("3000", "数据异常"),
    E_3001("3001", "数据版本过低，不再兼容"),
    E_3002("3002", "要求当前非空，但未获取到"),
    E_3003("3003", "要求当前为空，但能获取到"),
    E_3004("3004", "批量数据取得数与预期不一致"),
    E_3005("3005", "状态无法对应当前操作"),
    E_3006("3006", "配置信息解析失败"),
    E_3007("3007", "取值不在预定义的范围内"),
    E_3008("3008", "操作影响的数据量与预期不一致"),
    E_3009("3009", "数据变更因其他数据强约束被回滚"),
    /* 数据问题 end */

    /* http问题 start */
    E_4000("4000", "http请求异常"),
    E_4001("4001", "请求资源不存在"),
    E_4002("4002", "请求方法不支持"),
    /* http问题 end */

    /* 外部接口问题 start */
    E_5000("5000", "外部接口异常"),
    E_5001("5001", "外部接口调用失败,服务不可用", ExceptionConstant.FEATURE_RETRY),
    E_5002("5002", "外部接口调用超时", ExceptionConstant.FEATURE_RETRY),
    E_5003("5003", "外部接口成功响应，但抛出异常"),
    E_5004("5004", "外部接口调用成功，返回失败信息"),
    E_5005("5005", "外部接口成功响应，但未返回有意义结果"),
    /* 外部接口问题 start */

    /* 数据库问题 start */
    E_6000("6000", "数据库异常"),
    E_6001("6001", "数据库通信异常，无法发起调用", ExceptionConstant.FEATURE_RETRY),
    E_6002("6002", "获取数据库连接超时", ExceptionConstant.FEATURE_RETRY),
    E_6003("6003", "成功响应，但抛出异常"),
    E_6004("6004", "sql语句问题异常"),
    E_6005("6005","插入更新未通过约束性校验"),
    /* 数据库问题 start */

    /* 中间件问题 start */
    E_7000("7000", "中间件异常"),
//    E_7001("7001", "中间件调用失败，服务不可用", ExceptionConstant.FEATURE_RETRY),
//    E_7002("7002", "mq调用超时", ExceptionConstant.FEATURE_RETRY),
//    E_7003("7003", "mq成功响应，但抛出异常"),
//    E_7007("7007", "缓存发起调用失败，服务不可用", ExceptionConstant.FEATURE_RETRY),
//    E_7008("7008", "缓存调用超时", ExceptionConstant.FEATURE_RETRY),
//    E_7009("7009", "缓存成功响应，但抛出异常"),
//    E_7103("7103", "检索引擎发起调用失败，服务不可用", ExceptionConstant.FEATURE_RETRY),
//    E_7104("7104", "检索引擎调用超时", ExceptionConstant.FEATURE_RETRY),
//    E_7105("7105", "检索引擎成功响应，但抛出异常"),
    /* 中间件问题 end */

    /* 系统内部问题 start */
    E_9000("9000", "内部异常"),
    E_9001("9001", "内部RPC接口发起调用失败，服务不可用", ExceptionConstant.FEATURE_RETRY),
    E_9002("9002", "内部RPC接口超时", ExceptionConstant.FEATURE_RETRY),
    E_9003("9003", "内部RPC成功响应，但未返回有意义结果"),
    E_9004("9004", "接口运行到了异常埋点位置，但未查明原因"),
    E_9005("9005", "没有从现有系统流程中找到可用的处理方法"),
    E_9006("9006", "逻辑运行到了预留设计的功能块，但目前不支持"),
    E_9007("9007","未经程序设计允许可处理的空值"),
    /* 系统内部问题 end */;

    BaseErrorEnum(String errorType, String errorMessage) {
        init(errorType, errorMessage, ExceptionConstant.FEATURE_NONE);
    }

    BaseErrorEnum(String errorType, String errorMessage, int featureNumber) {
        init(errorType, errorMessage, featureNumber);
    }

    private void init(String errorType, String errorMessage, int featureNumber) {
        StringBuilder stringBuilder = new StringBuilder(errorType);
        stringBuilder.append(SYS_ID);
        stringBuilder.append(featureNumber);
        this.errorCode = stringBuilder.toString();
        this.errorMessage = errorMessage;
    }

    private String errorCode;
    private String errorMessage;
    public static final String SYS_ID = ExceptionConstant.SYS_ID_BASE;

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorDesc() {
        return errorMessage;
    }

    @Override
    public boolean standardError() {
        return true;
    }


}
