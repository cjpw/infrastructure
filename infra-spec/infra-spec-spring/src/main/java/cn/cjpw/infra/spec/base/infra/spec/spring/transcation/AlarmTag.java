package cn.cjpw.infra.spec.base.infra.spec.spring.transcation;

/**
 * @author jun.chen1
 * @since 2023/11/16 20:50
 **/
public interface AlarmTag {

    /**
     * 返回方便定位数据的特殊标识，比如tid，orderId等
     *
     * @return
     */
    String getSourceTid();

    /**
     * 用于区分不同的业务场景
     *
     * @return
     */
    String getRunnerTag();
}
