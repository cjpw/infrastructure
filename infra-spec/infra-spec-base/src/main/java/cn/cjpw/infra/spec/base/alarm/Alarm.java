package cn.cjpw.infra.spec.base.alarm;

/**
 * 通用告警器
 *
 * @author jun.chen1
 * @since 2021/12/30 14:54
 **/
public interface Alarm {

    /**
     * @param tag     告警场景标签
     * @param traceId 调用链id
     * @param content 告警内容
     */
    void alarm(String tag, String traceId, String content);
}
