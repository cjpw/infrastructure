package cn.cjpw.infra.spec.base.mock;

/**
 * @author jun.chen1
 * @since 2022/11/30 11:08
 **/
public interface RemoteApiMockConfiguration {

    /**
     * 模拟三方api全局调用开关
     *
     * @return
     */
    boolean mockRemoteApi();

    /**
     * 针对具体的mock方法判断mock开关。
     *
     * @param mockName 等价于{@link RemoteApiMock#mockName()}
     * @return
     */
    default boolean getOpenStatus(String mockName) {
        return mockRemoteApi();
    }
}
