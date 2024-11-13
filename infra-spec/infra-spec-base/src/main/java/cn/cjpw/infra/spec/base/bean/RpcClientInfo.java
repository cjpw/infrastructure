package cn.cjpw.infra.spec.base.bean;


/**
 * @author jun.chen1
 * @since 2021/5/7 17:38
 **/
public class RpcClientInfo {

    /**
     * 调用方集群分组
     */
    private String consumerFlag;
    /**
     * 调用方应用编码
     */
    private String consumerAppCode;
    /**
     * 调用方项目编码
     */
    private String consumerProCode;
    /**
     * 调用方ip
     */
    private String consumerHost;


    /**
     * 获取 调用方集群分组
     *
     * @return consumerFlag 调用方集群分组
     */
    public String getConsumerFlag() {
        return this.consumerFlag;
    }

    /**
     * 设置 调用方集群分组
     *
     * @param consumerFlag 调用方集群分组
     */
    public void setConsumerFlag(String consumerFlag) {
        this.consumerFlag = consumerFlag;
    }

    /**
     * 获取 调用方应用编码
     *
     * @return consumerAppCode 调用方应用编码
     */
    public String getConsumerAppCode() {
        return this.consumerAppCode;
    }

    /**
     * 设置 调用方应用编码
     *
     * @param consumerAppCode 调用方应用编码
     */
    public void setConsumerAppCode(String consumerAppCode) {
        this.consumerAppCode = consumerAppCode;
    }

    /**
     * 获取 调用方项目编码
     *
     * @return consumerProCode 调用方项目编码
     */
    public String getConsumerProCode() {
        return this.consumerProCode;
    }

    /**
     * 设置 调用方项目编码
     *
     * @param consumerProCode 调用方项目编码
     */
    public void setConsumerProCode(String consumerProCode) {
        this.consumerProCode = consumerProCode;
    }

    /**
     * 获取 调用方ip
     *
     * @return consumerHost 调用方ip
     */
    public String getConsumerHost() {
        return this.consumerHost;
    }

    /**
     * 设置 调用方ip
     *
     * @param consumerHost 调用方ip
     */
    public void setConsumerHost(String consumerHost) {
        this.consumerHost = consumerHost;
    }
}
