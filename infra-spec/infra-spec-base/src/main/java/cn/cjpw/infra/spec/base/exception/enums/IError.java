package cn.cjpw.infra.spec.base.exception.enums;


/**
 * 异常码定义接口 可以定义不同异常的错误码 枚举 但是都必须继承这个接口 设计标准语句<a href="https://duodian.feishu.cn/sheets/shtcndwFZI9FaNY3W1L63xJpDVg?sheet=2f0938">
 *
 * @author jun.chen1
 * @since 2020/6/18 10:27
 */
public interface IError {

    /**
     * 获取错误码，四位字符串
     *
     * @return
     */
    String getErrorCode();

    /**
     * 获取异常大类分类[0-9,A-Z]大小写敏感
     *
     * @return
     */
    default String getCodeType() {
        return getErrorCode().charAt(0) + "";
    }

    /**
     * 获取异常明细码[000-999]
     *
     * @return
     */
    default String getCodeSubType() {
        return getErrorCode().substring(1, 4);
    }

    /**
     * 判断异常是否属于统一类型
     * 匹配CodeType+CodeSubType
     *
     * @param source
     * @return
     */
    default boolean equalsCode(IError source) {
        return getCodeType().equals(source.getCodeType()) && getCodeSubType().equals(source.getCodeSubType());
    }

    /**
     * 标识错误来源的系统id [0-9A-Z] ""为未明确定义
     *
     * @return
     */
    default String getModuleId() {
        return "";
    }

    /**
     * 异常特征码
     * 是一种通用性设计，技术层面的约定，建议全局统一设计
     * <p>
     * 有效值>=0
     * 用bitmap表示，总共64位,每种特性占用位数由架构整体约定
     * <p>
     * 预定义应用场景：
     * 第一个特性表示该异常发生后，流量来源客户端是否可以重试，取值是否，占用1位。用于客户端自行判断是否需要进行重试而不强行回滚业务以提升用户体验。
     * 第二个特性表示该异常定义是否属于明确场景的异常，取值是否，占用1位。比如用户提交表单，异常提示xx字段必填，这种异常属于特定场景异常，由具体业务定制设计。\n
     * 如果异常提示字段校验不通过，这种异常比较宽泛，可以在多中非法输入场景使用。被标注该特性的异常，宽泛异常特性通常是架构统一捕获生成，\n
     * 可用于研发快速确认该异常是否当初已经被考虑到并正确处理返回给上游。
     *
     * @return
     */
    default Long getFeatureCode() {
        return -1L;
    }

    /**
     * 系统码
     * <p>
     * 如果是分布式系统，系统约定好自身唯一码值，便于长链路垮系统调用异常肉眼快速定位来源
     *
     * @return
     */
    default String getSysCode() {
        return "";
    }

    /**
     * 调用链编码
     * <p>
     * 如果系统已支持调用链码注入，透传回客户端时，方便快速利用该码值定位日志。
     * 当然，如果只是为了方便定位日志，在异常源头生成一个随机码也是可以的。
     *
     * @return
     */
    default String getTraceCode() {
        return "";
    }

    /**
     * 描述错误大致信息，不包含具体业务信息，如：参数错误，权限错误，系统错误等基础信息
     *
     * @return
     */
    String getErrorDesc();

    /**
     * 是否预定义异常类型
     * <p>
     * 如果是预定义错误，错误信息会被加工
     *
     * @return
     */
    default boolean standardError() {
        return false;
    }

    /**
     * 权限异常，泛化
     */
    IError PERMISSION_ERROR = BaseErrorEnum.E_1000;
    /**
     * 无接口调用权限
     */
    IError PERMISSION_METHOD_NO_AUTH = BaseErrorEnum.E_1001;
    /**
     * 无数据操作权限
     */
    IError PERMISSION_DATA_NO_AUTH = BaseErrorEnum.E_1002;
    /**
     * 权限信息获取失败
     */
    IError PERMISSION_GETTING_FAILURE = BaseErrorEnum.E_1003;
    /**
     * 账户状态异常
     */
    IError PERMISSION_ACCOUNT_STATUS_ABNORMAL = BaseErrorEnum.E_1004;
    /**
     * 登录状态异常
     */
    IError PERMISSION_LOGIN_STATUS_ABNORMAL = BaseErrorEnum.E_1005;
    /**
     * 授权信息已过时
     */
    IError PERMISSION_AUTH_TIMEOUT = BaseErrorEnum.E_1006;
    /**
     * 参数校验不通过
     */
    IError VALIDATION_ERROR = BaseErrorEnum.E_2000;
    /**
     * 入参基础校验不通过
     */
    IError VALIDATION_PARAM_FAIL = BaseErrorEnum.E_2001;
    /**
     * 参数解析、格式化失败
     */
    IError VALIDATION_PARSE_ERROR = BaseErrorEnum.E_2002;
    /**
     * 参数规则处理后不满足后续执行条件
     */
    IError VALIDATION_NEXT_CONDITION_DENY = BaseErrorEnum.E_2003;
    /**
     * 已过期的请求
     */
    IError VALIDATION_OVERDUE = BaseErrorEnum.E_2004;
    /**
     * 其他业务正锁定该资源，获取资源处理权失败
     */
    IError VALIDATION_RESOURCE_LOCKED = BaseErrorEnum.E_2005;
    /**
     * 不再兼容的参数值
     */
    IError VALIDATION_INCOMPATIBLE_PARAM = BaseErrorEnum.E_2006;
    /**
     * 不再兼容的接口调用
     */
    IError VALIDATION_INCOMPATIBLE_METHOD = BaseErrorEnum.E_2007;
    /**
     * 重复请求
     */
    IError VALIDATION_REPEATED_REQ = BaseErrorEnum.E_2008;
    /**
     * 数据异常
     */
    IError DATA_ERROR = BaseErrorEnum.E_3000;
    /**
     * 不兼容的旧数据
     */
    IError DATA_INCOMPATIBLE_RESULT = BaseErrorEnum.E_3001;
    /**
     * 要求非空数据但未获取到
     */
    IError DATA_ASSERT_NOT_EMPTY_FAILURE = BaseErrorEnum.E_3002;
    /**
     * 不应该存在的数据但获取到
     */
    IError DATA_ASSERT_EMPTY_FAILURE = BaseErrorEnum.E_3003;
    /**
     * 多条数据返回数量与预期不一致
     */
    IError DATA_ASSERT_COUNT_MATCH_FAILURE = BaseErrorEnum.E_3004;
    /**
     * 数据状态值与预期不一致
     */
    IError DATA_ASSERT_STATUS_MATCH_FAILURE = BaseErrorEnum.E_3005;
    /**
     * 用户配置信息不正确
     */
    IError DATA_USER_CONFIG_LOGIC = BaseErrorEnum.E_3006;
    /**
     * 数据结果值不在预定义的范围内
     */
    IError DATA_OUT_OF_BOUND = BaseErrorEnum.E_3007;
    /**
     * 操作影响的数据量与预期不符
     */
    IError DATA_INFLUENCE_COUNT_NOT_MATCH = BaseErrorEnum.E_3008;
    /**
     * 影响数据因其他数据强依赖关系被回滚
     */
    IError DATA_DEPENDENCE_ROLLBACK = BaseErrorEnum.E_3009;
    /**
     * http请求异常
     */
    IError HTTP_ERROR = BaseErrorEnum.E_4000;
    /**
     * 请求资源不存在
     */
    IError HTTP_URL_NOT_FOUND = BaseErrorEnum.E_4001;
    /**
     * 请求方法不支持
     */
    IError HTTP_METHOD_NOT_SUPPORT = BaseErrorEnum.E_4002;
    /**
     * 外部接口异常
     */
    IError REMOTE_API_ERROR = BaseErrorEnum.E_5000;
    /**
     * 发起调用失败,服务不可用
     */
    IError REMOTE_API_NO_RESPONSE = BaseErrorEnum.E_5001;
    /**
     * 接口调用超时
     */
    IError REMOTE_API_TIMEOUT = BaseErrorEnum.E_5002;
    /**
     * 成功响应，但抛出异常
     */
    IError REMOTE_API_THROW_EXCEPTION = BaseErrorEnum.E_5003;
    /**
     * 调用成功，返回失败信息
     */
    IError REMOTE_API_UNSUCCESSFUL_RESULT = BaseErrorEnum.E_5004;
    /**
     * 成功响应，但返回的序列化结果关键变量没有值
     */
    IError REMOTE_API_EMPTY_RESULT = BaseErrorEnum.E_5005;
    /**
     * 数据库异常
     */
    IError DB_ERROR = BaseErrorEnum.E_6000;
    /**
     * 数据库通信异常，无法发起调用
     */
    IError DB_NO_RESPONSE = BaseErrorEnum.E_6001;
    /**
     * 获取数据库连接超时
     */
    IError DB_CONNECTION_TIMEOUT = BaseErrorEnum.E_6002;
    /**
     * 成功响应，但抛出异常
     */
    IError DB_EXECUTE_THROW_EXCEPTION = BaseErrorEnum.E_6003;
    /**
     * sql语句问题异常
     */
    IError DB_SQL_ERROR = BaseErrorEnum.E_6004;
    /**
     * 插入更新未通过约束性校验
     */
    IError DB_DATA_INTEGRITY_VIOLATION = BaseErrorEnum.E_6005;
    //    /**
    //     * 中间件异常
    //     */
    //    IError MIDDLEWARE_ERROR = BaseErrorEnum.E_7000;
    //    /**
    //     * 发起调用失败，服务不可用
    //     */
    //    IError MQ_NO_RESPONSE = BaseErrorEnum.E_7001;
    //    /**
    //     * 调用超时
    //     */
    //    IError MQ_TIMEOUT = BaseErrorEnum.E_7002;
    //    /**
    //     * 成功响应，但抛出异常
    //     */
    //    IError MQ_THROW_EXCEPTION = BaseErrorEnum.E_7003;
    //    /**
    //     * 发起调用失败，服务不可用
    //     */
    //    IError CACHE_NO_RESPONSE = BaseErrorEnum.E_7007;
    //    /**
    //     * 调用超时
    //     */
    //    IError CACHE_TIMEOUT = BaseErrorEnum.E_7008;
    //    /**
    //     * 成功响应，但抛出异常
    //     */
    //    IError CACHE_THROW_EXCEPTION = BaseErrorEnum.E_7009;
    //    /**
    //     * 发起调用失败，服务不可用
    //     */
    //    IError FULLTEXT_NO_RESPONSE = BaseErrorEnum.E_7103;
    //    /**
    //     * 调用超时
    //     */
    //    IError FULLTEXT_TIMEOUT = BaseErrorEnum.E_7104;
    //    /**
    //     * 成功响应，但抛出异常
    //     */
    //    IError FULLTEXT_THROW_EXCEPTION = BaseErrorEnum.E_7105;
    /**
     * 内部异常
     */
    IError SYS_ERROR = BaseErrorEnum.E_9000;
    /**
     * 内部远程接口发起调用失败，服务不可用
     */
    IError SYS_INNER_API_NO_RESPONSE = BaseErrorEnum.E_9001;
    /**
     * 内部远程接口超时
     */
    IError SYS_INNER_API_TIMEOUT = BaseErrorEnum.E_9002;
    /**
     * 成功响应，但返回的序列化结果关键变量没有值
     */
    IError SYS_INNER_API_EMPTY_RESULT = BaseErrorEnum.E_9003;
    /**
     * 接口运行到了异常埋点位置，但未查明原因
     */
    IError SYS_RUNNING_IN_TRAP = BaseErrorEnum.E_9004;
    /**
     * 没有从现有系统流程中找到可用的处理方法
     */
    IError SYS_NO_HANDLER = BaseErrorEnum.E_9005;
    /**
     * 逻辑运行到了预留设计的功能块，但目前不支持
     */
    IError SYS_RESERVED_HANDLER = BaseErrorEnum.E_9006;
    /**
     * 未经程序设计允许可处理的空值
     */
    IError SYS_UN_HANDLED_NULL_VALUE = BaseErrorEnum.E_9007;

}
